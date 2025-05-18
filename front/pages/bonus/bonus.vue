<template>
  <view class="container">
    <!-- 待审批列表 -->
    <view class="approve-container">
      <view class="section-title">待审批奖金（{{ pendingList.length }}）</view>
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in pendingList" 
          :key="index" 
          class="list-item pending"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeName }}</text>
              <text class="department">工号：{{ item.employeeId }}</text>
            </view>
            
            <view class="detail-grid">
              <view class="detail-item">
                <text class="label">类型：</text>
                <text class="value">{{ item.category }}</text>
              </view>
              <view class="detail-item">
                <text class="label">金额：</text>
                <text class="amount">￥{{ item.amount }}</text>
              </view>
            </view>

            <view class="meta">
              <text class="time">申请时间：{{ item.applyTime }}</text>
            </view>
            
            <view class="action-btns">
              <button class="detail-btn" @tap="showDetail(item)">查看详情</button>
              <button class="approve-btn" @tap="showApproveDialog(item)">通过</button>
              <button class="reject-btn" @tap="showRejectDialog(item)">驳回</button>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 历史审批记录 -->
    <view class="section-divider">
      <text class="divider-text">历史审批记录</text>
      <view class="divider-line"></view>
    </view>
    
    <view class="history-container">
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
                <text class="value">{{ item.category }}</text>
              </view>
              <view class="detail-item">
                <text class="label">金额：</text>
                <text class="amount">￥{{ item.amount }}</text>
              </view>
            </view>

			<view class="meta">
			  <text class="time">申请时间：{{ item.applyTime }}</text>
            </view>

            <view class="meta">
			  <text class="time">审批时间：{{ item.dealTime }}</text>
              <text class="approver">审批人：{{ item.approver }}</text>
            </view>
            
            <view class="remark" v-if="item.remark">审批意见：{{ item.remark }}</view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 弹窗组件 -->
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

    <uni-popup ref="detailPopup" type="dialog">
      <uni-popup-dialog title="奖金详情" :show-cancel="false" confirm-text="关闭">
        <view class="detail-content">
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
            <text class="value">{{ currentDetail.category }}</text>
          </view>
          <view class="detail-item">
            <text class="label">金 额：</text>
            <text class="amount">￥{{ currentDetail.amount }}</text>
          </view>
          <view class="detail-item full">
            <text class="label">申请原因：</text>
            <text class="value">{{ currentDetail.reason }}</text>
          </view>
          <view class="detail-item">
            <text class="label">申请时间：</text>
            <text class="value">{{ currentDetail.applyTime }}</text>
          </view>
        </view>
      </uni-popup-dialog>
    </uni-popup>

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
	  loginer: '李娜', // 从登录信息获取实际值 **
      messageType: 'success',
      messageText: '',
      currentDetail: {},
      currentItem: null,
      pendingList: [],
      historyList: [],
      statusMap: {
        pending: '审批中',
        approved: '已通过',
        rejected: '已驳回'
      }
    }
  },
  async created() {
    await this.loadAllData()
  },
  methods: {
    // 加载所有数据
    async loadAllData() {
      try {
        await Promise.all([
          this.loadPendingList(),
          this.loadHistoryList()
        ])
      } catch (e) {
        this.showMessage('数据加载失败', 'error')
      }
    },

    // 加载待审批列表
    async loadPendingList() {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/fBonus/pending`,
          method: 'GET'
        })
        this.pendingList = res.data.data.map(this.formatItem)
      } catch (e) {
        this.showMessage('加载待审批列表失败', 'error')
      }
    },

    // 加载审批历史
    async loadHistoryList() {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/fBonus/approver/${this.loginer}`,
          method: 'GET'
        })
        this.historyList = res.data.data.map(this.formatItem)
      } catch (e) {
        this.showMessage('加载审批历史失败', 'error')
      }
    },

    // 格式化数据项
    formatItem(item) {
      return {
        id: item.id,
        employeeId: item.employeeId,
        employeeName: item.applicant,
        category: item.category,
        amount: Number(item.amount).toFixed(2),
        reason: item.reason,
        applyTime: this.formatTime(item.applicationTime),
        dealTime: this.formatTime(item.dealTime),
        status: this.mapStatus(item.approvalStatus),
        approver: item.approver,
        remark: item.approvalOpinion
      }
    },

    // 时间格式化
    formatTime(timestamp) {
      if (!timestamp) return '--'
      const date = new Date(timestamp)
      return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')} ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`
    },

    // 状态映射
    mapStatus(status) {
      switch(status) {
        case 0: return 'pending'
        case 1: return 'approved'
        case 2: return 'rejected'
        default: return ''
      }
    },

    // 显示审批弹窗
    showApproveDialog(item) {
      this.currentItem = item
      this.$refs.approveDialog.open()
    },
	// 显示驳回弹窗
	showRejectDialog(item) {
		this.currentItem = item; // 保存当前审批项
		this.$refs.rejectDialog.open();
	},

    // 处理通过审批
    async handleApprove(remark) {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/fBonus/approve/${this.currentItem.id}?approver=${encodeURIComponent(this.loginer)}&opinion=${encodeURIComponent(remark)}&status=${1}`,
          method: 'POST',
        })
    
          await this.loadAllData()
          this.showMessage('审批通过成功', 'success')
      } catch (e) {
        this.showMessage('审批操作失败', 'error')
      } finally {
        this.$refs.approveDialog.close()
      }
    },

    // 处理驳回审批
    async handleReject(remark) {
      if (!remark.trim()) {
        this.showMessage('必须填写驳回理由', 'error')
        return
      }

      try {
        const res = await uni.request({
          url: `${globalURL}/api/fBonus/approve/${this.currentItem.id}?approver=${encodeURIComponent(this.loginer)}&opinion=${encodeURIComponent(remark)}&status=${2}`,
          method: 'POST',
        })
        
          await this.loadAllData()
          this.showMessage('已驳回申请', 'success')

      } catch (e) {
        this.showMessage('操作失败', 'error')
      } finally {
        this.$refs.rejectDialog.close()
      }
    },

    // 显示详情
    showDetail(item) {
      this.currentDetail = item
      this.$refs.detailPopup.open()
    },

    // 显示消息提示
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
/* 样式复用报账审批页面 */
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
/* 
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
} */

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

.approve-btn {
  background: #e8f5e9;
  color: #4CAF50;
}

.reject-btn {
  background: #ffebee;
  color: #f44336;
}

/* 其他样式与报账审批页面保持一致 */
</style>