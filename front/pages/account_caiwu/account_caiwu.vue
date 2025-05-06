<template>
  <view class="container">
    <!-- 待审批列表 -->
    <view class="approve-container">
      <view class="section-title">待审批报账（{{ pendingList.length }}）</view>
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in pendingList" 
          :key="index" 
          class="list-item pending"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeName }}</text>
              <text class="department">{{ item.department }}</text>
            </view>
            
            <view class="detail-grid">
              <view class="detail-item">
                <text class="label">类型：</text>
                <text class="value">{{ item.reimburseType }}</text>
              </view>
              <view class="detail-item">
                <text class="label">单号：</text>
                <text class="value">{{ item.purchaseOrder }}</text>
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

    <!-- 审批历史 -->
    <view class="history-container">
      <view class="section-title">审批历史</view>
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in historyList" 
          :key="index" 
          class="list-item"
          :class="item.status"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeName }}</text>
              <text :class="['status', item.status]">{{ statusMap[item.status] }}</text>
            </view>
            
            <view class="detail-grid">
              <view class="detail-item">
                <text class="label">类型：</text>
                <text class="value">{{ item.reimburseType }}</text>
              </view>
              <view class="detail-item">
                <text class="label">单号：</text>
                <text class="value">{{ item.purchaseOrder }}</text>
              </view>
              <view class="detail-item">
                <text class="label">金额：</text>
                <text class="amount">￥{{ item.amount }}</text>
              </view>
            </view>

            <view class="meta">
              <text class="time">审批时间：{{ item.approveTime }}</text>
              <text class="approver">审批人：{{ item.approver }}</text>
            </view>
            
            <view class="remark" v-if="item.remark">审批意见：{{ item.remark }}</view>
          </view>
        </view>
      </scroll-view>
    </view>

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
            <text class="label">部 门：</text>
            <text class="value">{{ currentDetail.department }}</text>
          </view>
          <view class="detail-item">
            <text class="label">类 型：</text>
            <text class="value">{{ currentDetail.reimburseType }}</text>
          </view>
          <view class="detail-item">
            <text class="label">单 号：</text>
            <text class="value">{{ currentDetail.purchaseOrder }}</text>
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
export default {
  data() {
    return {
      currentItem: null,
      actionType: 'approve',
      messageType: 'success',
      messageText: '',
      applications: [
        {
          id: 1,
          employeeName: '李文文',
          department: '生产部',
          reimburseType: '零件采购',
          purchaseOrder: 'CG202307001',
          amount: 1500,
          details: '采购10套轴承配件',
          applyTime: '2023-07-20 14:30',
          status: 'pending'
        },
        {
          id: 2,
          employeeName: '王大力',
          department: '技术部',
          reimburseType: '内部采购',
          purchaseOrder: 'NB202307015',
          amount: 3000,
          details: '会议室投影设备采购',
          applyTime: '2023-07-18 09:15',
          status: 'approved',
          approver: '财务部-张会计',
          approveTime: '2023-07-19 10:00',
          remark: '票据齐全，符合报销标准'
        }
      ],
      statusMap: {
        pending: '待审批',
        approved: '已通过',
        rejected: '已驳回'
      },
	  // isShow:false,
    }
  },
  computed: {
    pendingList() {
      return this.applications.filter(item => item.status === 'pending')
    },
    historyList() {
      return this.applications.filter(item => item.status !== 'pending').reverse()
    }
  },
  methods: {
    showDetail(item) {
	  console.log('当前详情数据：',item)
      // this.currentDetail = { ...item }
	  this.currentDetail = {
	        employeeName: item.employeeName,
	        department: item.department, // 字段转换
	        reimburseType: item.reimburseType,
	        purchaseOrder: item.purchaseOrder,
	        amount: item.amount,
	        details: item.details,
	        applyTime: item.applyTime
	      }
	  console.log('当前详情数据：',this.currentDetail)
	  // this.isShow = true
      this.$refs.detailPopup.open()
    },
    showApproveDialog(item) {
      this.currentItem = item
      this.actionType = 'approve'
      this.$refs.approveDialog.open()
    },
    showRejectDialog(item) {
      this.currentItem = item
      this.actionType = 'reject'
      this.$refs.rejectDialog.open()
    },
    handleApprove(remark) {
      const index = this.applications.findIndex(i => i.id === this.currentItem.id)
      this.applications[index] = {
        ...this.currentItem,
        status: 'approved',
        approver: '财务部-张会计',
        approveTime: this.getCurrentTime(),
        remark: remark || '审批通过'
      }
      this.showMessage('审批通过', 'success')
      this.$refs.approveDialog.close()
    },
    handleReject(remark) {
      if (!remark.trim()) {
        uni.showToast({ title: '请填写驳回理由', icon: 'none' })
        return
      }
      
      const index = this.applications.findIndex(i => i.id === this.currentItem.id)
      this.applications[index] = {
        ...this.currentItem,
        status: 'rejected',
        approver: '财务部-张会计',
        approveTime: this.getCurrentTime(),
        remark: remark
      }
      this.showMessage('已驳回申请', 'error')
      this.$refs.rejectDialog.close()
    },
    getCurrentTime() {
      const now = new Date()
      return `${now.getFullYear()}-${(now.getMonth()+1).toString().padStart(2,'0')}-${now.getDate().toString().padStart(2,'0')} ${now.getHours().toString().padStart(2,'0')}:${now.getMinutes().toString().padStart(2,'0')}`
    },
    showMessage(text, type) {
      this.messageText = text
      this.messageType = type
      this.$refs.messagePopup.open()
      setTimeout(() => this.$refs.messagePopup.close(), 1500)
    }
  }
}
</script>

<style scoped>
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
  min-width: 100rpx;
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