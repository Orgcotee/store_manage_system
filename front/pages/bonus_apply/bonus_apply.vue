<template>
  <view class="container">
    <!-- 奖金申请表单 -->
    <view class="form-container">
      <!-- 申请人信息 -->
      <view class="form-item">
        <text class="form-label">申请人：</text>
        <view class="picker disabled-text">
          {{ loginer || '未获取到申请人信息' }}
        </view>
      </view>
      
      <view class="form-item">
        <text class="form-label">工号：</text>
        <view class="picker disabled-text">
          {{ loginerId || '未获取到申请人工号' }}
        </view>
      </view>

      <!-- 奖金类型选择 -->
      <view class="form-item">
        <text class="form-label">奖金类型：</text>
        <picker 
          @change="selectType" 
          :value="typeIndex" 
          :range="bonusTypes"
        >
          <view class="picker">
            {{ selectedType || '请选择类型' }}
          </view>
        </picker>
      </view>

      <!-- 金额输入 -->
      <view class="form-item">
        <text class="form-label">金额（元）：</text>
        <input 
          type="number" 
          v-model="formData.amount" 
          placeholder="请输入金额"
          class="input"
        />
      </view>

      <!-- 申请原因 -->
      <view class="form-item">
        <text class="form-label">申请原因：</text>
        <textarea 
          v-model="formData.reason" 
          placeholder="请输入详细申请理由"
          class="textarea"
          :maxlength="200"
        />
      </view>

      <!-- 附件上传 -->
      <view class="form-item">
        <text class="form-label">证明材料：</text>
        <button class="upload-btn" @tap="uploadImage">上传附件</button>
        <text class="file-name" v-if="formData.img">{{ fileName }}</text>
      </view>

      <button class="submit-btn" @tap="submitApplication">提交申请</button>
    </view>

    <!-- 历史记录 -->
    <view class="history-container">
      <view class="section-title">申请记录</view>
      
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in applications" 
          :key="index" 
          class="list-item"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeId }}{{ item.employeeName }}</text>
              <text :class="['status', item.approvalStatus]">{{ statusMap[item.approvalStatus] }}</text>
            </view>
           <view class="detail-row">
             <text class="label">类型：</text>
             <text class="value">{{ item.category }}</text> 
           </view>
            <view class="detail-row">
              <text class="label">金额：</text>
              <text class="amount">￥{{ item.amount }}</text>
            </view>
			<view class="meta">
			  <text class="time">申请时间：{{ item.applyTime }}</text>
			</view>
            <view class="meta" v-if="item.approvalStatus === 'approved'|| item.approvalStatus === 'rejected'">
              <text class="time">审批时间：{{ item.dealTime }}</text>
              <text class="approver" v-if="item.approver">审批人：{{ item.approver }}</text>
            </view>
            <view class="remark-approved" v-if="item.remark && item.approvalStatus === 'approved'">
              审批意见：{{ item.remark }}
            </view>
            <view class="remark-rejected" v-if="item.remark && item.approvalStatus === 'rejected'">
              审批意见：{{ item.remark }}
            </view>
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
import { globalURL } from '../../constant/config.js'

export default {
  data() {
    return {
      loginer: '张伟',     // 当前登录用户（需对接用户系统）
      loginerId: '1015',  // 当前用户ID
      bonusTypes: ['绩效奖金', '项目奖金', '特殊贡献奖', '季度奖金'], // 奖金类型
      typeIndex: -1,      // 选中的奖金类型索引
	  // selectedType:'',
      formData: {         // 表单数据
        amount: '',       // 金额
		bonusType: '', //奖金类型
        reason: '',       // 申请原因
        img: '',          // 附件路径
        applicant: '',    // 申请人（自动填充）
        employeeId: '',    // 员工ID（自动填充）
      },
      applications: [],   // 申请记录列表
      statusMap: {        // 状态映射
        pending: '审批中',
        approved: '已通过',
        rejected: '已驳回'
      },
      fileName: ''        // 上传文件名
    }
  },
  async created() {
    await this.loadApplications() // 加载申请记录
  },
  computed: {
    selectedType() {
      return this.typeIndex >= 0 
        ? this.bonusTypes[this.typeIndex]
        : ''
    }
  },
  methods: {
    /**
     * 加载申请记录
     */
    async loadApplications() {
      try {
        const res = await uni.request({
          url: `${globalURL}/api/fBonus/historyById/${this.loginerId}`,
          method: 'GET',
        })
        
		console.log("获取到的数据是：",res.data.data);
		
        // if (res.data.code === 200) {
          this.applications = res.data.data.map(item => ({
            id: item.id,
            employeeId: item.employeeId,
            employeeName: item.applicant,
			category: item.category,
            reason: item.reason,
            amount: item.amount,
            applyTime: this.formatDate(item.applicationTime),
            approvalStatus: this.mapStatus(item.approvalStatus),
            approver: item.approver,
            remark: item.approvalOpinion,
			dealTime: this.formatDate(item.dealTime),
          }));
        // }
      } catch (e) {
        console.error('加载记录失败:', e)
        uni.showToast({ title: '加载记录失败', icon: 'none' })
      }
    },

    /**
     * 提交申请
     */
    async submitApplication() {
      if (!this.validateForm()) return

      const payload = {
		category: this.selectedType, // 使用正确的奖金类型字段
        applicant: this.loginer,
        employeeId: this.loginerId,
        amount: Number(this.formData.amount).toFixed(2),
        reason: this.formData.reason,
        img: this.formData.img,
        approvalStatus: 0 // 初始状态为待审批
      }

      try {
        const [error, res] = await uni.request({
          url: `${globalURL}/api/fBonus/create`,
          method: 'POST',
          data: payload,
          header: { 'Content-Type': 'application/json' }
        })

        if (res.data.code === 200) {
          this.$refs.successPopup.open()
          await this.loadApplications() // 刷新列表
          this.resetForm()
        }
      } catch (e) {
        uni.showToast({ title: '提交失败，请重试', icon: 'none' })
      }
    },

    /**
     * 表单验证
     */
    validateForm() {
	  if (!this.formData.bonusType) {
		uni.showToast({ title: '请选择奖金类型', icon: 'none' })
		return false
	  }
      if (!this.formData.reason.trim()) {
        uni.showToast({ title: '请输入申请原因', icon: 'none' })
        return false
      }
      if (!this.formData.amount || Number(this.formData.amount) <= 0) {
        uni.showToast({ title: '请输入有效金额', icon: 'none' })
        return false
      }
      return true
    },

    /**
     * 上传附件
     */
    async uploadImage() {
      try {
        const [chooseRes] = await uni.chooseImage({ count: 1 })
        const file = chooseRes.tempFilePaths[0]
        
        const [uploadRes] = await uni.uploadFile({
          url: `${globalURL}/api/upload`,
          filePath: file,
          name: 'file'
        })
        
        this.formData.img = JSON.parse(uploadRes.data).data
        this.fileName = file.name.substring(0, 20) // 限制文件名显示长度
      } catch (e) {
        uni.showToast({ title: '上传失败', icon: 'none' })
      }
    },

    /**
     * 重置表单
     */
    resetForm() {
      this.typeIndex = -1
      this.formData = {
        amount: '',
        reason: '',
        img: '',
		bonusType:'',
		employeeId:this.loginerId,
		applicant:this.loginer,
      }
      this.fileName = ''
    },

    /**
     * 时间格式化
     */
    formatDate(date) {
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')} ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`
    },

    /**
     * 状态映射
     */
    mapStatus(status) {
      switch(status) {
        case 0: return 'pending'
        case 1: return 'approved'
        case 2: return 'rejected'
        default: return ''
      }
    },

    /**
     * 选择奖金类型
     */
    selectType(e) {
      this.typeIndex = e.detail.value;
	  this.formData.bonusType = this.bonusTypes[this.typeIndex];
    }
  }
}
</script>

<style scoped>
/* 复用报账页面的样式 */
/* 新增置灰样式 */
.disabled-text {
  color: #999 !important;         /* 浅灰色文字 */
  background-color: #f5f5f5;      /* 浅灰色背景 */
  cursor: not-allowed;            /* 禁用光标 */
  opacity: 0.9;                   /* 轻微透明效果 */
}
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

.remark-rejected {
  color: #f44336;
}

.remark-approved {
  color: #666;
}
/* 新增上传按钮样式 */
.upload-btn {
  background: #f8f9fa;
  border: 1rpx solid #e0e0e0;
  color: #666;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 28rpx;
}

.file-name {
  color: #999;
  font-size: 24rpx;
  margin-left: 20rpx;
}
</style>