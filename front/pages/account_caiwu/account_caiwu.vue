<template>
  <view class="container">
    <!-- 待审批列表 -->
    <view class="approve-container">
      <!-- <view class="section-title">待审批报账（{{ pendingList.length }}）</view> -->
	   <view class="section-title">待审批报账</view>
      <scroll-view scroll-y class="list-container">
        <!-- 加载状态 -->
        <!-- <uni-load-more v-if="loading" status="loading"></uni-load-more> -->
        
        <!-- 空状态 -->
        <view v-if="!loading && pendingList.length === 0" class="empty-tip">
          <!-- <image src="/static/images/empty.png" class="empty-img"/> -->
          <text class="empty-text">当前没有待审批的报账申请</text>
        </view>

        <!-- 待审批列表内容 -->
        <view 
          v-for="(item, index) in pendingList" 
          :key="index" 
          class="list-item pending"
        >
          <!-- 列表项结构保持不变 -->
		    <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeName }}</text>
              <text class="department">工号：{{ item.employeeId }}</text>
            </view>
            
            <view class="detail-grid">
              <view class="detail-item">
                <text class="label">类型：</text>
                <text class="value">{{ item.reimburseType }}</text>
              </view>
              <view class="detail-item">
                <text class="label">金额：</text>
                <text class="amount">￥{{ item.amount }}</text>
              </view>
            </view>

            <view class="meta">
              <text class="time">申请时间：{{ item.applyTime }}</text>
            </view>
            
            <view class="details" v-if="item.details">详情：{{ item.details }}</view>
            
            <view class="action-btns">
              <button class="detail-btn" @tap="showDetail(item)">查看详情</button>
              <button class="approve-btn" @tap="showApproveDialog(item)">通过</button>
              <button class="reject-btn" @tap="showRejectDialog(item)">驳回</button>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

	<!-- 新增分隔区域 -->
	  <view class="section-divider">
		<text class="divider-text">历史审批记录</text>
		<view class="divider-line"></view>
	  </view>
	  
    <!-- 审批历史 -->
    <view class="history-container">
      <!-- <view class="section-title">审批历史</view> -->
      <scroll-view scroll-y class="list-container">
        <!-- 历史记录内容 -->
		 <view 
          v-for="(item, index) in historyList" 
          :key="index" 
          class="list-item"
      
        >
		    <!-- :class="item.status" -->
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{item.employeeId}}{{ item.employeeName }}</text>
              <text :class="['status', item.status]">{{ statusMap[item.status] }}</text>
            </view>
            
            <view class="detail-grid">
              <view class="detail-item">
                <text class="label">类型：</text>
                <text class="value">{{ item.reimburseType }}</text>
              </view>
              <view class="detail-item">
                <text class="label">金额：</text>
                <text class="amount">￥{{ item.amount }}</text>
              </view>
            </view>
			<view class="meta">
			  <text class="approver">审批人：{{ item.approver }}</text>
			</view>
			<view class="meta">
			  <text class="time">详情：{{ item.details }}</text>
			</view>
			<view class="meta">
              <text class="time">申请时间：{{ item.applyTime }}</text>
            </view>
            <view class="meta">
              <text class="time">审批时间：{{ item.approveTime }}</text>
            </view>
            
            <view class="remark" v-if="item.remark">审批意见：{{ item.remark }}</view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 弹窗组件保持不变 -->
	 <!-- 审批弹窗 -->
	    <uni-popup ref="approveDialog" type="dialog">
	      <uni-popup-dialog 
	        mode="input" 
	        title="审批通过"
	        placeholder="请输入审批备注（可选）"
	        @confirm="handleApprove"
	      />
	    </uni-popup>
	
	    <uni-popup ref="rejectDialog" type="dialog">
	      <uni-popup-dialog 
	        mode="input" 
	        title="审批驳回"
	        placeholder="请输入驳回理由"
	        @confirm="handleReject"
	      />
	    </uni-popup>
	
	    <!-- 详情弹窗 -->
	    <uni-popup ref="detailPopup" type="dialog">
	      <uni-popup-dialog title="报账详情" :show-cancel="false" confirm-text="关闭">
	        <view class="detail-content" >
	          <view class="detail-item">
	            <text class="label">申请人：</text>
	            <text class="value">{{ currentDetail.employeeName }}</text>
	          </view>
			  <view class="detail-item">
			    <text class="label">工号：</text>
			    <text class="value">{{ currentDetail.employeeId }}</text>
			  </view>
	          <view class="detail-item">
	            <text class="label">类 型：</text>
	            <text class="value">{{ currentDetail.reimburseType }}</text>
	          </view>
	          <view class="detail-item">
	            <text class="label">金 额：</text>
	            <text class="amount">￥{{ currentDetail.amount }}</text>
	          </view>
	          <view class="detail-item full">
	            <text class="label">详 情：</text>
	            <text class="value">{{ currentDetail.details }}</text>
	          </view>
	          <view class="detail-item">
	            <text class="label">申请时间：</text>
	            <text class="value">{{ currentDetail.applyTime }}</text>
	          </view>
			   <!-- 附件预览 -->
			 <!-- <view class="attachments" v-if="currentDetail.imgUrls.length > 0">
				<text class="label">附件证明：</text>
				<view class="img-list">
				  <image 
					v-for="(img, index) in currentDetail.imgUrls" 
					:key="index"
					:src="img"
					mode="aspectFill"
					class="preview-img"
					@click="previewImage(img)"
				  />
				</view>
			  </view> -->
	        </view>
	      </uni-popup-dialog>
	    </uni-popup>
	
	    <!-- 消息提示 -->
	    <uni-popup ref="messagePopup" type="message">
	      <uni-popup-message :type="messageType" :message="messageText" />
	    </uni-popup>
  </view>
</template>

<script>
import { globalURL } from '../../constant/config.js'
export default {
  data() {
    return {
	  imgUrls:'',
	  messageType: 'success',
      messageText: '',
      loginer: '李娜', // 从登录信息获取实际值 **
      loading: true,
      currentDetail: [],
	  currentItem: null,
      // staffMap: {}, // 缓存员工部门信息
      pendingList: [],
      historyList: [],
      statusMap: {
        pending: '审批中',
        approved: '已通过',
        rejected: '已驳回'
      },
	   statusTextMap: {  // 新增状态文本映射
        0: '待审批',
        1: '已通过',
        2: '已驳回'
      }
    }
  },
  async created() {
    await this.loadStaffData();
    await this.loadAllData();
  },
  created() {
	  this.loadAllData();
  },
  methods: {
	  // 显示通过弹窗
	  showApproveDialog(item) {
		this.currentItem = item; // 保存当前审批项
		console.log('当前通过数据：',this.currentItem);
		this.$refs.approveDialog.open();
	  },

	  // 显示驳回弹窗
	  showRejectDialog(item) {
		this.currentItem = item; // 保存当前审批项
		this.$refs.rejectDialog.open();
	  },
	  // 显示详情弹窗
	  showDetail(item) {
		  console.log("申请时间：",item);
		this.currentDetail = {
		  ...item,
		  // 处理图片字段
		  imgUrls: item.img ? item.img.split(',') : []
		};
		this.$refs.detailPopup.open();
	  },

	  // 预览图片（新增方法）
	  previewImage(url) {
		uni.previewImage({
		  current: url,
		  urls: this.currentDetail.imgUrls
		});
	  },
    // 加载员工数据
    async loadStaffData() {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/fStaff`,
          method: 'GET'
        });
        this.staffMap = res.data.data.reduce((map, staff) => {
          map[staff.name] = staff.department;
          return map;
        }, {});
      } catch (e) {
        console.error('加载员工数据失败:', e);
      }
    },
    // 加载所有业务数据
    async loadAllData() {
      this.loading = true;
      try {
      // 先清空列表确保加载效果可见
      this.pendingList = [];
      this.historyList = [];
    
      // 并行加载
      await Promise.all([
      this.loadPendingList(),
      this.loadHistoryList()
      ]);
    
      // 强制视图更新
     this.$nextTick(() => {
           this.$forceUpdate();
           uni.pageScrollTo({ scrollTop: 0 }); // 滚动到顶部
         });
		console.log('全部数据加载完成');
      } finally {
        this.loading = false;
      }
    },
    // 加载待审批列表
    async loadPendingList() {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/reimbursement/pending`,
          method: 'GET'
        });
		console.log("待审批原始数据：",res.data.data)
        this.pendingList = res.data.data.map(item => this.formatItem(item));
		console.log('pendinglist:',this.pendingList);
      } catch (e) {
        this.showMessage('加载待审批列表失败', 'error');
      }
	  
    },

    // 加载审批历史
    async loadHistoryList() {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/reimbursement/approver/${this.loginer}`,
          method: 'GET'
        });
		console.log("审批历史：",res.data.data)
		this.historyList = res.data.data.map(item => this.formatItem(item));
      } catch (e) {
        this.showMessage('加载审批历史失败', 'error');
      }
    },

    // 统一格式化数据项
    formatItem(item) {
		// console.log("正在处理：",item);
		const result = {
			id: item.id,
			employeeId: item.applicantId,
			employeeName: item.applicant,
			// department: this.staffMap[item.applicant] || '未知部门',
			reimburseType: item.category,
			amount:Number(item.amount || 0).toFixed(2),
			details: item.details,
			applyTime: this.formatTime(item.applicationTime),
			status:item.status,
			// status:item.status,
			approver: item.approver,
			approveTime: this.formatTime(item.dealTime),
			remark: item.approvalOpinion,
			img: item.img,
			inOut:item.inOut,
		}
	  return result;
    },

    // 时间格式化
    formatTime(timestamp) {
      if (!timestamp) return '--';
      const date = new Date(timestamp);
      return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')} ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`;
    },

// 审批通过
  async handleApprove(remark) {
	  const confirmed = await uni.showModal({
	    title: '确认通过审批',
	    content: '确定要通过该申请吗？'
	  });
	  if (!confirmed) return;
	  console.log('审批人为：',this.loginer);
    try {
      uni.showLoading({ title: '处理中...', mask: true });
      
	  // console.log('审批人为：',loginer);
      const res = await uni.request({
        url: `${globalURL}/api/reimbursement/approve/${this.currentItem.id}?approver=${encodeURIComponent(this.loginer)}&remark=${encodeURIComponent(remark)}`,
        method: 'POST',
      });
      console.log(res.data.code);
      if (res.data.code === 200) {
		 // 清空当前操作项
      this.currentItem = null;
      // 刷新数据
      await this.loadAllData();
      // 显示成功提示
      this.showMessage('审批通过成功', 'success');
      // 强制滚动刷新
      // uni.pageScrollTo({ scrollTop: 0, duration: 300 });
	  
	  // 一秒后执行
	  // setTimeout(()=>{
	  // 	// 通过之后返回上一页
	  // 	that.back();	
	  // },1000)
      }
    } catch (e) {
      this.showMessage('审批操作失败', 'error');
    } finally {
      uni.hideLoading();
      this.$refs.approveDialog.close();
    }
  },

  // 审批驳回
  async handleReject(reason) {
	  
    if (!reason?.trim()) {
      uni.showToast({ title: '必须填写驳回理由', icon: 'none' });
      return;
    }

	const confirmed = await uni.showModal({
	  title: '确认驳回审批',
	  content: '确定要驳回该申请吗？'
	});
	if (!confirmed) return;
	
    try {
      uni.showLoading({ title: '处理中...', mask: true });
      
      const res = await uni.request({
        url: `${globalURL}/api/reimbursement/reject/${this.currentItem.id}?approver=${encodeURIComponent(this.loginer)}&reason=${encodeURIComponent(reason)}`,
        method: 'POST',
      });
      
      if (res.data.code === 200) {
		// 清空当前操作项
      this.currentItem = null;
      // 刷新数据
      await this.loadAllData();
      // 显示成功提示
       this.showMessage('已驳回申请', 'success');
      // 强制滚动刷新
      // uni.pageScrollTo({ scrollTop: 0, duration: 300 });
	  // 一秒后执行
	  setTimeout(()=>{
	  	// 驳回之后返回上一页
	  	that.back();	
	  },1000)
      }
    } catch (e) {
      this.showMessage('操作失败', 'error');
    } finally {
      uni.hideLoading();
      this.$refs.rejectDialog.close();
    }
  },

    // 显示消息提示
    showMessage(text, type) {
      this.messageText = text;
      this.messageType = type;
      this.$refs.messagePopup.open();
      setTimeout(() => this.$refs.messagePopup.close(), 1500);
    }
  }
}
</script>

<style scoped>
/* 附件样式 */
.attachments {
  margin-top: 40rpx;
  border-top: 1rpx solid #eee;
  padding-top: 30rpx;
}

.img-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-top: 20rpx;
}

.preview-img {
  width: 200rpx;
  height: 200rpx;
  border-radius: 8rpx;
  background: #f5f5f5;
}
.history-container {
  margin-top: 20rpx;
}
/* 新增分隔样式 */
.section-divider {
  position: relative;
  margin: 40rpx 0;
  padding: 0 20rpx;
}

.divider-text {
  position: relative;
  z-index: 1;
  display: inline-block;
  padding: 0 20rpx;
  background: #f8f9fa;
  color: #909399;
  font-size: 28rpx;
  transform: translateX(20rpx);
}

.divider-line {
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 2rpx;
  background: #e5e5e5;
  transform: translateY(-50%);
}
/* 新增空状态样式 */
.empty-tip {
  padding: 100rpx 0;
  text-align: center;
}
.empty-img {
  width: 200rpx;
  height: 200rpx;
  opacity: 0.6;
}
.empty-text {
  display: block;
  color: #888;
  font-size: 28rpx;
  margin-top: 20rpx;
}

/* 其他样式保持不变 */

.container {
  padding: 20rpx;
  background-color: #f8f9fa;
}

.section-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 30rpx;
  padding-left: 10rpx;
  border-left: 6rpx solid #2196F3;
}

.list-container {
  max-height: 60vh;
}

.list-item {
  padding: 25rpx;
  margin-bottom: 25rpx;
  background: #fff;
  border-radius: 8rpx;
  border: 1rpx solid #eee;
}

.list-item.pending {
  border-color: #2196F3;
  background: #f8f9ff;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.applicant {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.department {
  color: #666;
  font-size: 24rpx;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15rpx;
  margin-bottom: 20rpx;
}

.detail-item {
  display: flex;
  align-items: center;
}

.label {
  color: #666;
  font-size: 26rpx;
  min-width: 140rpx;
}

.value {
  color: #333;
  font-size: 26rpx;
}

.amount {
  color: #ff9800;
  font-size: 32rpx;
  font-weight: bold;
}

.meta {
  color: #999;
  font-size: 24rpx;
  margin: 15rpx 0;
}

.details {
  color: #666;
  font-size: 26rpx;
  line-height: 1.6;
  margin: 15rpx 0;
}

.action-btns {
  display: flex;
  gap: 20rpx;
  margin-top: 25rpx;
}

.detail-btn {
  flex: 1;
  background: #f0f0f0;
  color: #666;
  height: 70rpx;
  line-height: 70rpx;
  font-size: 26rpx;
}

.approve-btn {
  flex: 1;
  color: #5cb85c; 
  background: #eaffea;
  height: 70rpx;
  line-height: 70rpx;
}

.reject-btn {
  flex: 1;
  color: #d9534f; 
  background: #ffeceb;
  height: 70rpx;
  line-height: 70rpx;
}
.status {
  font-size: 24rpx;
  padding: 6rpx 15rpx;
  border-radius: 4rpx;
}

.status.pending {
  background: #e3f2fd;
  color: #2196F3;
}

.status.approved {
  background: #e8f5e9;
  color: #4CAF50;
}

.status.rejected {
  background: #ffebee;
  color: #f44336;
}

/* 详情弹窗样式 */
.detail-content {
  padding: 20rpx;
}

.detail-item {
  margin-bottom: 20rpx;
}

.detail-item.full {
  grid-column: 1 / -1;
}

.detail-item .label {
  width: 120rpx;
  color: #666;
}

.detail-item .value {
  flex: 1;
  word-break: break-all;
}
</style>