<template>
  <view class="container">
    <!-- 审批列表 -->
    <view class="approve-container">
      <view class="section-title">待审批申请（{{ pendingList.length }}）</view>
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in pendingList" 
          :key="index" 
          class="list-item pending"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">申请人：{{ item.applicant }}</text>
              <text class="department">{{ item.department }}</text>
            </view>
            <view class="detail">
              <text class="employee">{{ item.employeeName }}</text>
              <text class="type">{{ item.bonusType }}</text>
              <text class="amount">￥{{ item.amount }}</text>
            </view>
            <view class="meta">
              <text class="time">{{ item.applyTime }}</text>
            </view>
            <view class="reason">申请理由：{{ item.reason }}</view>
            <view class="action-btns">
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
              <text class="employee">{{ item.employeeName }}</text>
              <text :class="['status', item.status]">{{ statusMap[item.status] }}</text>
            </view>
            <view class="detail">
              <text class="applicant">申请人：{{ item.applicant }}</text>
              <text class="type">{{ item.bonusType }}</text>
              <text class="amount">￥{{ item.amount }}</text>
            </view>
            <view class="meta">
              <text class="time">{{ item.approveTime || item.applyTime }}</text>
              <text class="approver" v-if="item.approver">审批人：{{ item.approver }}</text>
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
        placeholder="请输入审批意见（可选）"
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

    <!-- 操作反馈 -->
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
          applicant: '生产部-李经理',
          department: '生产部',
          employeeName: '李文文',
          bonusType: '绩效奖金',
          amount: 1500,
          reason: 'Q2超额完成生产指标',
          applyTime: '2023-07-20 14:30',
          status: 'pending'
        },
        {
          id: 2,
          applicant: '技术部-王总监',
          department: '技术部',
          employeeName: '王大力',
          bonusType: '项目奖金',
          amount: 3000,
          reason: 'A项目提前交付',
          applyTime: '2023-07-18 09:15',
          status: 'approved',
          approver: '财务部-张会计',
          approveTime: '2023-07-19 10:00',
          remark: '符合项目奖金发放标准'
        }
      ],
      statusMap: {
        pending: '待审批',
        approved: '已通过',
        rejected: '已驳回'
      }
    }
  },
  computed: {
    pendingList() {
      return this.applications.filter(item => item.status === 'pending')
    },
    historyList() {
      return this.applications.filter(item => item.status !== 'pending')
    }
  },
  methods: {
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
      if (!this.validateRemark(remark)) return
      
      const index = this.applications.findIndex(i => i.id === this.currentItem.id)
      this.applications[index] = {
        ...this.currentItem,
        status: 'approved',
        approver: '财务部-张会计', // 实际应从用户信息获取
        approveTime: this.getCurrentTime(),
        remark: remark || '同意发放'
      }
      
      this.showMessage('审批通过', 'success')
      this.$refs.approveDialog.close()
    },
    handleReject(remark) {
      if (!this.validateRemark(remark, true)) return
      
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
    validateRemark(remark, isRequired = false) {
      if (isRequired && !remark.trim()) {
        uni.showToast({ title: '请填写驳回理由', icon: 'none' })
        return false
      }
      return true
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

.approve-container, .history-container {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
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
  border-radius: 8rpx;
  border: 1rpx solid #eee;
}

.list-item.pending {
  background: #f8f9ff;
  border-color: #2196F3;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.applicant, .employee {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.department {
  color: #666;
  font-size: 24rpx;
}

.detail {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin: 15rpx 0;
}

.type {
  color: #666;
  font-size: 26rpx;
}

.amount {
  color: #ff9800;
  font-size: 32rpx;
  font-weight: bold;
}

.meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 24rpx;
}

.reason, .remark {
  color: #666;
  font-size: 26rpx;
  margin-top: 15rpx;
  line-height: 1.6;
}

.remark {
  color: #f44336;
}

.action-btns {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.approve-btn, .reject-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 28rpx;
  border-radius: 8rpx;
}

.approve-btn {
  color: #5cb85c; 
  background: #eaffea;
}

.reject-btn {
  color: #d9534f; 
  background: #ffeceb;
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
</style>