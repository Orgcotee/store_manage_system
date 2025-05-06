<template>
  <view class="container">
    <!-- 订单状态及基础信息 -->
    <view class="status-card">
      <text class="status-text">{{ statusMap[orderData.status] }}</text>
      <view class="amount-box">
        <text class="amount-label">申请金额</text>
        <text class="amount">¥{{ orderData.amount }}</text>
      </view>
    </view>

    <!-- 订单详细信息 -->
    <uni-card class="finance-card">
      <view class="info-item">
        <text class="label">订单编号</text>
        <text class="value">{{ orderData.id }}</text>
      </view>
      
      <view class="info-item">
        <text class="label">申请人</text>
        <text class="value">{{ orderData.applicant }}</text>
      </view>
     
      <view class="info-item">
        <text class="label">所属部门</text>
        <text class="value">{{ orderData.department }}</text>
      </view>
	  
	  <view class="info-item">
	    <text class="label">申请时间</text>
	    <text class="value">{{ orderData.createTime }}</text>
	  </view>
	  
	  <view class="info-item">
	    <text class="label">供应商</text>
	    <text class="value">{{ orderData.forSupplier }}</text>
	  </view>

	  <view class="info-item">
        <text class="label">费用类型</text>
        <text class="value">{{ orderData.expenseType }}</text>
      </view>
	  <view class="info-item">
        <text class="label">是否有账期</text>
        <text class="value">{{ orderData.pay_static }}</text>
      </view>

	  <view class="info-item" v-if="orderData.pay_static==='是'">
        <text class="label">账期时间</text>
        <text class="value">{{ orderData.final_pay_time }}</text>
      </view>
	 <!-- <view class="info-item" >
	    <text class="label">det</text>
	    <text class="value">{{ orderData.orderDetails}}</text>
	  </view> -->
	  
    


      <!-- 审批流程 -->
	  <!-- 0-审批中 -->
	  <view class="approval-flow" v-if="orderData.status === 0">
	    <view class="flow-item" v-for="(step, index) in approvalSteps0" :key="index">
	      <view class="flow-icon" :class="step.status"></view>
	      <text class="flow-text">{{ step.text }}</text>
	    </view>
	  </view>
	  <!-- 1-已驳回 -->
	  <view class="approval-flow" v-if="orderData.status === 1">
	    <view class="flow-item" v-for="(step, index) in approvalSteps2" :key="index">
	      <view class="flow-icon" :class="step.status"></view>
	      <text class="flow-text">{{ step.text }}</text>
	    </view>
	  </view>
	  <!-- 1-待支付 -->
      <view class="approval-flow" v-if="orderData.status < 6 && orderData.status > 1">
        <view class="flow-item" v-for="(step, index) in approvalSteps1" :key="index">
          <view class="flow-icon" :class="step.status"></view>
          <text class="flow-text">{{ step.text }}</text>
        </view>
      </view>
    </uni-card>

    <!-- 费用明细 -->
    <uni-card class="finance-card">
      <view class="section-title">费用明细</view>
      <view class="detail-item" v-for="(item, index) in orderData.orderDetails" :key="index">
        <text class="item-name">{{ item.name }}</text>
        <text class="item-amount">¥{{ item.price }}</text>
      </view>
      <view class="total-amount">
        <text>总金额</text>
        <text>¥{{ orderData.amount}}</text>
      </view>
    </uni-card>

	<!-- 合同 -->
    <uni-card class="finance-card" v-if="orderData.contracts.length > 0 && orderData.status === 7">
      <view class="section-title">合同详情</view>
      <scroll-view scroll-x class="invoice-scroll">
        <image 
          v-for="(contract, index) in orderData.contracts"
          :key="index"
          :src="contract"
          class="invoice-image"
          mode="aspectFill"
          @click="previewContract(index)"
        ></image>
      </scroll-view>
    </uni-card>
	
    <!-- 发票信息 -->
    <uni-card class="finance-card" v-if="orderData.invoices.length > 0 && orderData.status === 7">
      <view class="section-title">发票凭证</view>
      <scroll-view scroll-x class="invoice-scroll">
        <image 
          v-for="(invoice, index) in orderData.invoices"
          :key="index"
          :src="invoice"
          class="invoice-image"
          mode="aspectFill"
          @click="previewInvoice(index)"
        ></image>
      </scroll-view>
    </uni-card>
	

    <!-- 操作按钮 -->
	<!-- 0-外部待审批 -->
    <view class="action-btns" v-if="orderData.status === 0">
      <button class="btn reject" @click="handleAudit('reject')">驳回申请</button>
      <button class="btn approve" @click="handleAudit('approve')">通过审批</button>
    </view>
	
	<!-- 1-驳回原因 -->
	<uni-card class="finance-card" v-if="orderData.status === 1">
	  <view class="reject-reason">
	    <text class="reason-title">驳回原因：</text>
	    <text>{{ orderData.reason || '未填写原因' }}</text>
	  </view>
	</uni-card>
	<!-- 1-已驳回 -->
	<view class="action-btns" v-if="orderData.status === 1">
	  <button class="btn reject" @click="handleAudit('repeal')">撤销驳回</button>
	</view>
	<!-- 2-待签订 -->
	<view class="action-btns" v-if="orderData.status === 2">
		<!-- 到合同页面有个发送合同给供应商（供应商确认后2->3）（这里财务只是发送，不改变订单状态） -->
	  <button class="btn action" @click="gotoContract(orderData.id)">去签合同</button> 
	  <button class="btn reject" @click="handleAudit('backout')">撤销通过</button>
	</view>
	<!-- 供应商确认 -->
	<!-- 3-签订中/待确认 -->
	<view class="action-btns" v-if="orderData.status === 3">
	  <button class="btn reject" @click="handleAudit('affirm')">确认签订</button> 
	</view>
	<!-- 4-待支付（已签订） -->
	<view class="action-btns" v-if="orderData.status === 4">
	  <button class="btn action">去支付</button>
	  <button class="btn action" @click="handleAudit('payment')">账期支付</button>
	</view>
	<!-- 支付完成后改为是"pay"状态 -->
	<!-- 5-账期中（已签订但未付款） -->
	<view class="action-btns" v-if="orderData.status === 5">
	  <button class="btn action">去支付</button>
	</view>
	
	<!-- 6-履行中（已支付，需采购部门确认交易完成） -->
	<!-- 改为“complete”的action状态 -->
	
	<!-- 7-已完成 -->
	<view class="action-btns" v-if="orderData.status === 7">
	  <button class="btn action" @click="gotoInvoice(orderData.id)">开具发票</button>
	</view>
  </view>
</template>

<script>
import { globalURL } from '../../constant/config.js'
export default {
  data() {
    return {
	  orderId: null, // 接收的订单ID
      // 订单状态映射
      statusMap: {
        0: '待审批',
        1: '已驳回',
        2: '待签订',
        3: '签订中',
        4: '待支付',
		5: '账期中',
		6: '履行中',
		7: '已完成'
      },

      
	  orderData: { // 清空模拟数据，改为空结构
        id: null,
		createTime: '',
		status: 0,
        // number: '',
        applicant: '',
        department: '',
		period:0,
		deadline:'',
		collectPay:0,
		approvalProcess:0,
		reason:'',
		inOut:0,
        forSupplier:0,
        pay_static: '',
        final_pay_time: '',//格式化deadline
        expenseType: '',
        amount: 0,
        orderDetails: [],
        invoices: [],
        contracts: [],
        // rejectReason: ''
      },
	  approvalSteps0: [] // 保持原有流程数据
      // 审批流程模拟数据
	  // approvalSteps0: [
	  //   { text: '提交申请', status: 'completed' },
	  //   { text: '部门审批', status: 'completed' },
	  //   { text: '财务审批', status: 'current' }
	  // ],
   //    approvalSteps2: [
   //      { text: '提交申请', status: 'completed' },
   //      { text: '部门审批', status: 'completed' },
   //      { text: '财务审批', status: 'rejected' }
   //    ],
	  // approvalSteps1: [
	  //   { text: '提交申请', status: 'completed' },
	  //   { text: '部门审批', status: 'completed' },
	  //   { text: '财务审批', status: 'completed' }
	  // ]
    }
  },

 onLoad(options) {
	 console.log(options.orderId)
    // 接收订单ID参数
	console.log("这是options",options.orderId)
	console.log("这是orderId",this.orderId)
    this.orderId = options.orderId
	console.log("再次确认",this.orderId)
    this.loadOrderData()
  },
  methods: {
	   async loadOrderData() {
	        uni.showLoading({ title: '加载中...' })
			console.log(this.orderId,"传过来的订单id是");
			// console.log('请求URL:', `/api/fOrder/${this.orderId}`);
	        try {
	          const res = await uni.request({
	            url:  `${globalURL}/api/fOrder/`+ this.orderId,
	            method: 'GET'
	          })
	          console.log("data是",res.data.data);
	          if (res.statusCode === 200) {
	            this.transformData(res.data.data)
	          } else {
				  
	            // uni.showToast({ title: '数据加载失败', icon: 'none' })
	          }
	        } catch (e) {
	          // uni.showToast({ title: '网络错误', icon: 'none' })
	        } finally {
	          uni.hideLoading()
	        }
	      },
		   // 转换后端数据为前端格式
		      transformData(apiData) {
		        this.orderData = {
		          ...apiData,
		          // number: apiData.id.toString(), // 假设订单号直接使用id
		          // supplier: apiData.forSupplierName,
		          pay_static: apiData.period === 1 ? '是' : '否',
		          final_pay_time: this.formatDate(apiData.deadline),
		          expenseType: this.getExpenseType(apiData.collectPay),
		          createTime: this.formatDate(apiData.createTime),
		          // orderDetails: orderData.orderDetails.map(item => ({
		          //   name: item.name,
		          //   amount: item.price * item.num
		          // })),
		          amount: apiData.orderDetails.reduce((sum, item) => sum + item.price * item.num, 0).toFixed(2)
		        }
		      },
		      formatDate(timestamp) {
				  // 实现日期格式化逻辑
				        const date = new Date(timestamp)
				        return `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`
				      },
			  getExpenseType(typeCode) {
				        return typeCode === 1 ? '支出' : '收入' // 根据collectPay字段转换
				      },
    // 处理审批操作
    // handleAudit(action) {
    //   if (action === 'approve') {
    //     this.orderData.status = 2
    //     uni.showToast({ title: '审批通过', icon: 'success' })
    //   } else {
    //     uni.showModal({
    //       title: '输入驳回原因',
    //       editable: true,
    //       success: res => {
    //         if (res.confirm) {
    //           this.orderData.status = 1
    //           this.orderData.reason = res.content
    //           uni.showToast({ title: '已驳回申请', icon: 'success' })
    //         }
    //       }
    //     })
    //   }
    // },
	async handleAudit(action) {
		let newStatus;
		let approvalProcess;
		const currentStatus = this.orderData.status;
		console.log("当前的状态current:",currentStatus);
		switch(currentStatus){
			case 0: // 待审批
			      if (action === 'approve') {
			        newStatus = 2; // 通过->待签订
			        approvalProcess = 1;
			      } else if (action === 'reject') {
			        newStatus = 1; // 驳回->已驳回
			        approvalProcess = 0;
			      }
			      break;
			      
			    case 1: // 已驳回
			      if (action === 'repeal') {
			        newStatus = 0; // 撤销驳回->待审批
			        approvalProcess = 0;
			      }
			      break;
			      
			    case 2: // 待签订
			      if (action === 'backout') {
			        newStatus = 0; // 撤销通过->待审批
			      }
			      break;
				case 3: // 签订中
				      if (action === 'affirm') {
				        newStatus = 4; // 确认签订->待支付
				      }
				      break;
				      
				case 4: // 待支付
				  if (action === 'pay') {
				    newStatus = 6; // 支付->履行中
				  } else if (action === 'payment') {
				    newStatus = 5; // 设置账期->账期中
				  }
				  break;
				  
				case 5: // 账期中
				  if (action === 'pay') {
				    newStatus = 6; // 支付->履行中
				  }
				  break;  
				case 6: // 履行中
				      if (action === 'complete') {
				        newStatus = 7; // 完成->已完成
				      }
				break;
				      
				default:
				    uni.showToast({ title: '当前状态不支持此操作', icon: 'none' });
					return;	
		}
	  try {
		  console.log("新状态；新审批进程",newStatus,approvalProcess);
	    const updateData = {
	      id: this.orderId,
	      status: newStatus,
	      approvalProcess: approvalProcess,
	    };
	
	    if (action === 'reject') {
	      const res = await uni.showModal({
	        title: '输入驳回原因',
	        editable: true
	      });
	      if (res.confirm) {
	        updateData.reason = res.content;
	      } else {
	        return;
	      }
	    }
	
	    await uni.request({
	      url: `${globalURL}/api/fOrder/update`,
	      method: 'POST',
	      data: updateData
	    });
	
	    this.loadOrderData(); // 刷新数据
	    uni.showToast({ title: action === 'approve' ? '操作成功' : '操作成功', icon: 'success' });
	  } catch (e) {
	    uni.showToast({ title: e.message || '操作失败', icon: 'none' });
	  }
	},
	// 跳转到开具发票凭证的页面
	gotoInvoice(id){
			//传递参数订单号number=id
			uni.navigateTo({
				url: '/pages/invoice/invoice?orderId='+ id
			});
			// uni.navigateTo({
			// 	// url: '../orderDetail/orderDetail?orders='+encodeURIComponent(JSON.stringify(this.orders))
			// });
		},
	gotoContract(id){
			//传递参数订单号number=id
			uni.navigateTo({
				url: '/pages/contract_caiwu/contract_caiwu?orderId='+ id
			});
			// uni.navigateTo({
			// 	// url: '../orderDetail/orderDetail?orders='+encodeURIComponent(JSON.stringify(this.orders))
			// });
		},	

    // 预览发票图片
    previewInvoice(index) {
      uni.previewImage({
        current: index,
        urls: this.orderData.invoices
      })
    },
	// 预览发票图片
	previewContract(index) {
	  uni.previewImage({
	    current: index,
	    urls: this.orderData.contracts
	  })
	}
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f8f8;
  padding: 20rpx;
}

.status-card {
  background: #ffffff;
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
}

.status-text {
  font-size: 36rpx;
  color: #2E8B57;
  font-weight: bold;
}

.amount-box {
  margin-top: 30rpx;
}

.amount-label {
  font-size: 28rpx;
  color: #666;
  margin-right: 20rpx;
}

.amount {
  font-size: 40rpx;
  color: #e64340;
  font-weight: bold;
}

.finance-card {
  margin: 20rpx 0;
  border-radius: 12rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  font-size: 28rpx;
}

.label {
  color: #666;
  min-width: 160rpx;
}

.value {
  color: #333;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  font-size: 28rpx;
}

.total-amount {
  display: flex;
  justify-content: space-between;
  padding: 30rpx 0;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.invoice-scroll {
  white-space: nowrap;
  padding: 20rpx 0;
}

.invoice-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.action-btns {
  display: flex;
  justify-content: space-around;
  padding: 30rpx;
  background: white;
}

.btn {
  width: 45%;
  height: 80rpx;
  line-height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.action {
  background: #fff;
  color: #2E8B57;
  border: 1rpx solid #2E8B57;
}

.reject {
  background: #ffeceb;
  color: #d9534f;
  border: 1rpx solid #d9534f;
}

.approve {
  background: #e5f5e5;
  color: #09bb07;
  border: 1rpx solid #09bb07;
}

.approval-flow {
  padding: 30rpx 0;
  display: flex;
  justify-content: space-around;
}

.flow-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
/* 审批进度条颜色 */
.flow-icon {
  width: 50rpx;
  height: 50rpx;
  border-radius: 50%;
  margin-bottom: 10rpx;
}

.flow-icon.completed {
  background: #09bb07;
}

.flow-icon.rejected {
  background: darkred;
}

.flow-icon.current {
  background: yellowgreen;
}

.reject-reason {
  padding: 20rpx;
  background: #fff0f0;
  border-radius: 8rpx;
  color: #d9534f;
}
</style>