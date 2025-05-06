<template>
  <view class="container">
    <!-- 报账表单 -->
    <view class="form-container">
      <view class="form-item">
        <text class="form-label">申请人：</text>
        <picker 
          @change="selectEmployee" 
          :value="selectedIndex" 
          :range="employees"
          range-key="name"
        >
          <view class="picker">
            {{ selectedEmployee.name || '请选择申请人' }}
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">报账类型：</text>
        <picker 
          @change="selectType" 
          :value="typeIndex" 
          :range="reimburseTypes"
        >
          <view class="picker">
            {{ selectedType || '请选择类型' }}
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">采购单号：</text>
        <input 
          v-model="formData.purchaseOrder" 
          placeholder="请输入采购单号"
          class="input"
        />
      </view>

      <view class="form-item">
        <text class="form-label">金额（元）：</text>
        <input 
          type="number" 
          v-model="formData.amount" 
          placeholder="请输入金额"
          class="input"
        />
      </view>

      <view class="form-item">
        <text class="form-label">报账详情：</text>
        <textarea 
          v-model="formData.details" 
          placeholder="请输入详细报账内容"
          class="textarea"
          :maxlength="200"
        />
      </view>

      <button class="submit-btn" @tap="submitApplication">提交申请</button>
    </view>

    <!-- 历史记录 -->
    <view class="history-container">
      <view class="section-title">报账记录</view>
      
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in applications" 
          :key="index" 
          class="list-item"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeName }}</text>
              <text :class="['status', item.status]">{{ statusMap[item.status] }}</text>
            </view>
            <view class="detail-row">
              <text class="label">类型：</text>
              <text class="value">{{ item.reimburseType }}</text>
            </view>
            <view class="detail-row">
              <text class="label">单号：</text>
              <text class="value">{{ item.purchaseOrder }}</text>
            </view>
            <view class="detail-row">
              <text class="label">金额：</text>
              <text class="amount">￥{{ item.amount }}</text>
            </view>
            <view class="meta">
              <text class="time">{{ item.applyTime }}</text>
              <text class="approver" v-if="item.approver">审批人：{{ item.approver }}</text>
            </view>
            <view class="details" v-if="item.details">详情：{{ item.details }}</view>
            <view class="remark" v-if="item.remark">审批意见：{{ item.remark }}</view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 提交提示 -->
    <uni-popup ref="successPopup" type="message">
      <uni-popup-message type="success" message="提交成功" />
    </uni-popup>
  </view>
</template>

<script>
export default {
  data() {
    return {
      employees: [
        { id: 1, name: '李文文', department: '生产部' },
        { id: 2, name: '张慧慧', department: '采购部' },
        { id: 3, name: '王大力', department: '技术部' }
      ],
      reimburseTypes: ['零件采购', '日用品采购', '内部采购'],
      selectedIndex: -1,
      typeIndex: -1,
      formData: {
        employeeId: '',
        employeeName: '',
        reimburseType: '',
        purchaseOrder: '',
        amount: '',
        details: ''
      },
      applications: [
        {
          id: 1,
          employeeName: '李文文',
          reimburseType: '零件采购',
          purchaseOrder: 'CG202307001',
          amount: 1500,
          details: '采购10套轴承配件',
          applyTime: '2023-07-20 14:30',
          status: 'approved',
          approver: '财务部-张会计',
          remark: '票据已核实，同意报销'
        },
        {
          id: 2,
          employeeName: '王大力',
          reimburseType: '内部采购',
          purchaseOrder: 'NB202307015',
          amount: 3000,
          details: '会议室投影设备采购',
          applyTime: '2023-07-18 09:15',
          status: 'pending'
        }
      ],
      statusMap: {
        pending: '审批中',
        approved: '已通过',
        rejected: '已驳回'
      }
    }
  },
  computed: {
    selectedEmployee() {
      return this.selectedIndex >= 0 
        ? this.employees[this.selectedIndex]
        : {}
    },
    selectedType() {
      return this.typeIndex >= 0
        ? this.reimburseTypes[this.typeIndex]
        : ''
    }
  },
  methods: {
    selectEmployee(e) {
      this.selectedIndex = e.detail.value
      this.formData.employeeId = this.employees[this.selectedIndex].id
      this.formData.employeeName = this.employees[this.selectedIndex].name
    },
    selectType(e) {
      this.typeIndex = e.detail.value
      this.formData.reimburseType = this.reimburseTypes[this.typeIndex]
    },
    validateForm() {
      if (!this.formData.employeeId) {
        uni.showToast({ title: '请选择申请人', icon: 'none' })
        return false
      }
      if (!this.formData.reimburseType) {
        uni.showToast({ title: '请选择报账类型', icon: 'none' })
        return false
      }
      if (!this.formData.purchaseOrder.trim()) {
        uni.showToast({ title: '请输入采购单号', icon: 'none' })
        return false
      }
      if (!this.formData.amount || Number(this.formData.amount) <= 0) {
        uni.showToast({ title: '请输入有效金额', icon: 'none' })
        return false
      }
      if (!this.formData.details.trim()) {
        uni.showToast({ title: '请输入报账详情', icon: 'none' })
        return false
      }
      return true
    },
    submitApplication() {
      if (!this.validateForm()) return

      const newApplication = {
        id: Date.now(),
        ...this.formData,
        applyTime: this.getCurrentTime(),
        status: 'pending'
      }

      this.applications.unshift(newApplication)
      this.resetForm()
      this.$refs.successPopup.open()
      setTimeout(() => this.$refs.successPopup.close(), 1500)
    },
    getCurrentTime() {
      const now = new Date()
      return `${now.getFullYear()}-${(now.getMonth()+1).toString().padStart(2,'0')}-${now.getDate().toString().padStart(2,'0')} ${now.getHours().toString().padStart(2,'0')}:${now.getMinutes().toString().padStart(2,'0')}`
    },
    resetForm() {
      this.selectedIndex = -1
      this.typeIndex = -1
      this.formData = {
        employeeId: '',
        employeeName: '',
        reimburseType: '',
        purchaseOrder: '',
        amount: '',
        details: ''
      }
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  background-color: #f8f9fa;
}

.form-container {
  background: white;
  padding: 30rpx;
  border-radius: 12rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  color: #666;
  font-size: 28rpx;
  margin-bottom: 15rpx;
}

.picker, .input, .textarea {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.textarea {
  height: 200rpx;
  padding: 20rpx;
}

.submit-btn {
  background: #2196F3;
  color: white;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 8rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
}

.history-container {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
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

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.applicant {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
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

.detail-row {
  display: flex;
  align-items: center;
  margin: 10rpx 0;
}

.label {
  color: #666;
  font-size: 26rpx;
  min-width: 120rpx;
}

.value {
  color: #333;
  font-size: 26rpx;
}

.amount {
  color: #ff9800;
  font-size: 30rpx;
  font-weight: bold;
}

.meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 24rpx;
  margin-top: 15rpx;
}

.details, .remark {
  color: #666;
  font-size: 26rpx;
  margin-top: 15rpx;
  line-height: 1.6;
}

.remark {
  color: #f44336;
}
</style>