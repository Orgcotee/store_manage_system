<template>
  <view class="container">
    <!-- 分段导航 -->
    <view class="uni-padding-wrap">
      <uni-segmented-control 
        :current="current" 
        :values="tabs" 
        @clickItem="switchTab"
        styleType="text" 
        activeColor="#2E8B57"
      ></uni-segmented-control>
    </view>

    <!-- 订单列表 -->
    <scroll-view 
      scroll-y="true" 
      class="scroll-view"
      v-for="(tab, index) in tabs" 
      v-show="current === index"
      :key="index"
    >
      <!-- 无数据提示 -->
      <view v-if="filteredOrders.length === 0" class="empty-tip">
        暂无相关订单
      </view>

      <!-- 订单卡片 -->
      <view v-for="order in filteredOrders" :key="order.id">
        <uni-card class="finance-card">
          <!-- 卡片头部 -->
          <view class="card-header">
            <view class="title-wrap">
              <text class="order-no">订单号：{{ order.id }}</text>
              <text class="order-time">{{ order.createTime }}</text>
            </view>
            <text :class="['status', statusClass[order.status]]">
              {{ statusText[order.status] }}
            </text>
          </view>

          <!-- 分割线 -->
          <view class="divider"></view>

          <!-- 内容区域 -->
          <view class="content" v-if="order.inOut=== 1">
            <view class="info-item">
              <text class="label">申请人：</text>
              <text>{{ order.applicant }}</text>
            </view>
            
            <view class="info-item">
              <text class="label">部门：</text>
              <text>{{ order.department }}</text>
            </view>
			
			<view class="info-item">
			  <text class="label">供应商：</text>
			  <text>{{ order.forSupplier }}</text>
			</view> 
			
			<view class="info-item">
			 <text class="label">收支类型：</text>
			 <text>{{ order.collectPayText }}</text>
			</view>
			
			<view class="info-item">
			 <text class="label">账期状态：</text>
			 <text>{{ order.periodText }}</text>
			</view>

			<view class="info-item" v-if="order.periodText === '有账期'">
			 <text class="label">账期结束日期：</text>
			 <text>{{ order.deadline }}</text>
			</view>
			
			

            <view class="amount-wrap">
              <text class="label">总金额：</text>
              <text class="amount">¥{{ order.totalAmount.toFixed(2) }}</text>
            </view>
			
		 <!-- 商品列表 -->
        <view class="goods-list">
          <view class="goods-item" v-for="(item, index) in order.orderDetails" :key="index">
            <text>{{ item.name }} × {{ item.num }}</text>
            <text>¥{{ (item.price * item.num).toFixed(2) }}</text>
          </view>
        </view>
		
            <!-- 票据预览 -->
            <scroll-view scroll-x class="invoice-scroll">
              <image 
                v-for="(invoice, i) in order.invoices" 
                :key="i"
                :src="invoice"
                class="invoice-image"
                mode="aspectFill"
                @click="previewImage(order.invoices, i)"
              ></image>
            </scroll-view>
          </view>

          <!-- 操作按钮 -->
		  <view class="action-btns">
			   <button class="btn detail" @click="gotoOrderDetail(order.id)">订单详情</button>
		  </view>
       <!--  <view class="action-btns" v-if="order.status === 0">
            <button class="btn reject" @click="handleAudit(order.id, 'reject')">驳回</button>
            <button class="btn approve" @click="handleAudit(order.id, 'approve')">通过</button>
          </view> -->
        </uni-card>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { globalURL } from '../../constant/config.js'
export default {
  data() {
    return {
      tabs: ['待审批', '已驳回', '待签订','签订中','待支付','履行中','已完成'],  // 导航标签
      current: 0,  // 当前选中标签索引
      statusText: {  // 状态文本映射
        0: '待审批',
        1: '已驳回',
        2: '待签订',
        3: '签订中',
        4: '待支付',
		5: '账期中',
		6: '履行中',
		7: '已完成',
      },
      statusClass: {  // 状态样式映射
        0: 'pending',
        1: 'rejected',
		2: 'approved',
		3: 'during',
		4: 'waiting',
        5: 'final_pay',
        6: 'paid',
		7: 'completed',
      },
      // 模拟财务订单数据
      orders: []
    }
  },

  computed: {
    // 过滤后的订单列表
    filteredOrders() {
      switch (this.current) {
        case 0: return this.orders.filter(o => o.status === 0)  // 待审批
        case 1: return this.orders.filter(o => o.status === 1)  // 已驳回
		case 2: return this.orders.filter(o => o.status === 2)  // 待签订
		case 3: return this.orders.filter(o => o.status === 3)  // 签订中
		case 4: return this.orders.filter(o => o.status === 4)  // 待支付/已签订
		// case 5: return this.orders.filter(o => o.status === 5)  // 账期中
		case 5: return this.orders.filter(o => o.status === 5 || o.status === 6)  // 履行中(账期中和已付款)
		case 6: return this.orders.filter(o => o.status === 7)  // 已完成
        default: return this.orders  // 全部
      }
    }
  },
  onShow() {
	  	this.getOrdersAll();
		uni.$on('refreshData',() => {
				this.getOrdersAll();
			})
	  },
  onLoad() {
  this.getOrdersAll();
  // 添加刷新监听
  uni.$on('refreshOrderList', () => {
    this.getOrdersAll();
  });
   },
  onUnload() {
  // 移除监听
  uni.$off('refreshOrderList');
  },
  methods: {
	  // 获取订单数据（匹配您的后端接口）
	    async getOrdersAll() {
	       uni.showLoading({ title: '加载中...' });
	       
	       try {
	         const res = await uni.request({
	              url: `${globalURL}/api/fOrder/getAllOrder`,
	              method: 'GET'
	            });
	         
	         console.log('完整响应:', res); // 调试用
			 
	         const response = Array.isArray(res) ? res[1] : res;
			 
	         if (res.statusCode === 200) {
	           this.orders = response.data.data.map(order => ({
	                  ...order,
	                  createTime: this.formatDate(order.createTime),
	                  deadline: order.deadline ? this.formatDate(order.deadline) : '无',
	                  periodText: order.period === 1 ? '有账期' : '无账期',
	                  collectPayText: order.collectPay === 1 ? '支出' : '收入',
	                  totalAmount: (order.orderDetails || []).reduce((sum, item) => sum + (item.price * item.num), 0)
	                }));
	              } else {
	                throw new Error(`请求失败: ${response.statusCode}`);
	              }
	            } catch (error) {
	              console.error('获取订单失败:', error);
	              uni.showToast({
	                title: error.message,
	                icon: 'none'
	              });
	            } finally {
	              uni.hideLoading();
	            }
	     },
	    
	    // 日期格式化方法
	    formatDate(dateStr) {
	      if (!dateStr) return '';
	      const date = new Date(dateStr);
	      return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
	    },
    // 切换标签
    switchTab(e) {
      if (this.current !== e.currentIndex) {
        this.current = e.currentIndex
      }
    },
	// getOrdersAll(){
	// 	url.request({
	// 		url:'http://localhost:8080/api/fOrder/getAllOrder',
	// 		method:'GET',
	// 		success:(res) => {
	// 			console.log(res.data,'查询到')
	// 			let a = res.data.data.length;
	// 		}
	// 	})
	// },
	gotoOrderDetail(id) {
	  // 找到对应的订单对象
	  const order = this.orders.find(o => o.id === id);
	  console.log("----",order)
	  if (order) {
	    uni.navigateTo({
			// console.log
	      url: `/pages/order_detail_caiwu/order_detail_caiwu?orderId=${encodeURIComponent(JSON.stringify(order.id))}`
		  // url: '/pages/order_detail_caiwu/order_detail_caiwu?orderId='+ id
	    });
	  }
	},
	// gotoOrderDetail(id){
	// 		//传递参数订单号number=id
	// 		uni.navigateTo({
	// 			url: '/pages/order_detail_caiwu/order_detail_caiwu?orderId='+ id
	// 		});
	// 		// uni.navigateTo({
	// 		// 	// url: '../orderDetail/orderDetail?orders='+encodeURIComponent(JSON.stringify(this.orders))
	// 		// });
	// 	},
    // // 处理审批
    // handleAudit(orderId, action) {
    //   const order = this.orders.find(o => o.id === orderId)
    //   if (order) {
    //     order.status = action === 'approve' ? 1 : 2
    //     uni.showToast({
    //       title: action === 'approve' ? '审批通过' : '已驳回',
    //       icon: 'success'
    //     })
    //   }
    // },

    // 预览图片
    previewImage(urls, index) {
      uni.previewImage({
        current: index,
        urls: urls
      })
    }
  }
}
</script>

<style scoped>
.goods-list {
  margin-top: 20rpx;
  border-top: 1rpx dashed #eee;
  padding-top: 20rpx;
}

.goods-item {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
  font-size: 26rpx;
  border-bottom: 1rpx solid #f5f5f5;
}
.container {
  background-color: #f8f8f8;
  min-height: 100vh;
  padding: 20rpx;
}

.uni-padding-wrap {
  padding: 20rpx 30rpx;
  background: white;
}

.finance-card {
  margin: 20rpx;
  border-radius: 12rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20rpx;
}

.order-no {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
}

.order-time {
  font-size: 24rpx;
  color: #666;
  margin-top: 10rpx;
  display: block;
}

.status {
  font-size: 26rpx;
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
}

/* 状态颜色 */
.pending { color: #f0ad4e; background: #fcf8e3; }
.approved { color: #f0ad4e; background: #fcf8e3;}
.rejected { color: #d9534f; background: #ffeceb; }
.during { color: #337ab7; background: #eef7ff; }
.waiting { color: #337ab7; background: #eef7ff; }
.paid { color: #5cb85c; background: #eaffea; }
.completed { color: #999; background: #f5f5f5; }
.final_pay { color: #337ab7; background: #eef7ff; }


.divider {
  height: 1rpx;
  background-color: #eee;
  margin: 20rpx 0;
}

.info-item {
  display: flex;
  margin: 15rpx 0;
  font-size: 28rpx;
}

.label {
  color: #666;
  min-width: 140rpx;
}

.amount-wrap {
  margin: 25rpx 0;
  padding: 20rpx;
  background: #f8f8f8;
  border-radius: 8rpx;
}

.amount {
  font-size: 36rpx;
  color: #e64340;
  font-weight: bold;
}

.invoice-scroll {
  white-space: nowrap;
  margin-top: 20rpx;
}

.invoice-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.action-btns {
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
  margin-top: 30rpx;
}

.btn {
  font-size: 28rpx;
  padding: 0 40rpx;
  height: 60rpx;
  line-height: 60rpx;
  border-radius: 40rpx;
}

.detail {
  background: #fff;
  color: #2E8B57;
  border: 1rpx solid #2E8B57;
}

.reject {
  background: #fff0f0;
  color: #e64340;
  border: 1rpx solid #e64340;
}

.approve {
  background: #e5f5e5;
  color: #09bb07;
  border: 1rpx solid #09bb07;
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 40rpx;
  font-size: 28rpx;
}
</style>