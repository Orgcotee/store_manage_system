<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-box">
      <input 
        v-model="searchName" 
        placeholder="请输入姓名搜索" 
        class="search-input"
        placeholder-style="color:#999"
      />
      <button @tap="addEmployee" class="add-btn">+ 新增员工</button>
    </view>
	<view v-if="filteredEmployees.length === 0" class="empty-tips">
	    没有找到匹配的员工
	  </view>
    <!-- 员工列表 -->
    <scroll-view scroll-y class="list-container">
      <view 
        v-for="(item, index) in filteredEmployees" 
        :key="index" 
        class="list-item"
      >
       <!-- <view class="avatar">
          <uni-icons type="person" size="28" color="#fff"></uni-icons>
        </view> -->
        <view class="info-container">
		  <text class="name">{{ item.id }}</text>
		   <text class="separator">|</text>
          <text class="name">{{ item.name }}</text>
          <view class="details">
            <text class="department">{{ item.department }}</text>
            <text class="separator">|</text>
            <text class="phone">{{ item.phone }}</text>
          </view>
        </view>
        <view class="action-container">
          <button @tap="showDetail(item)" class="action-btn detail">员工详情</button>
          <button @tap="editEmployee(index)" class="action-btn edit">编辑</button>
          <button @tap="deleteEmployee(item.id)" class="action-btn delete">删除</button>
        </view>
      </view>
    </scroll-view>

    <!-- 编辑弹窗 -->
    <uni-popup ref="editPopup" type="dialog">
      <uni-popup-dialog 
        :title="isNew ? '新增员工' : '编辑员工'"
        @confirm="saveEmployee"
      >
        <view class="form-container">
          <view class="form-item">
            <text class="label">姓名：</text>
            <input v-model="currentEmployee.name" class="form-input" />
          </view>
         <!-- <view class="form-item">
            <text class="label">工号：</text>
			<input v-model="currentEmployee.id" class="form-input" :disabled="!isNew" />
          </view> -->
          <view class="form-item">
            <text class="label">部门：</text>
            <input v-model="currentEmployee.department" class="form-input" />
          </view>
          <view class="form-item">
            <text class="label">职位：</text>
            <input v-model="currentEmployee.position" class="form-input" />
          </view>
         <!-- <view class="form-item">
            <text class="label">入职日期：</text>
            <input 
              v-model="currentEmployee.serviceTime" 
              type="date" 
              class="form-input"
			  :value="formatDate(currentEmployee.serviceTime)"
			  @input="currentEmployee.serviceTime = $event.detail.value"
            />
          </view> -->
		 <view class="form-item">
		     <text class="label">入职日期：</text>
		  <picker 
		    mode="date" 
		    :value="currentEmployee.serviceTime" 
		    @change="dateChange"
		  >
		    <view class="form-input">
		      {{ formatDate(currentEmployee.serviceTime) || "请选择员工入职日期"}}
		    </view>
		  </picker>
		  </view>
		  <view class="form-item">
            <text class="label">手机号：</text>
            <input v-model="currentEmployee.phone" class="form-input" type="number" />
          </view>
          <view class="form-item">
            <text class="label">银行卡号：</text>
            <input v-model="currentEmployee.bank" class="form-input" type="number" />
          </view>
          <view class="form-item">
            <text class="label">邮箱：</text>
            <input v-model="currentEmployee.email" class="form-input" type="email" />
          </view>
        </view>
      </uni-popup-dialog>
    </uni-popup>
	
	<!-- 新增详情弹窗 -->
	  <uni-popup ref="detailPopup" type="dialog">
	    <uni-popup-dialog title="员工详情" :show-cancel="false" confirm-text="关闭">
	      <view class="detail-container">
	        <view class="detail-item">
	          <text class="detail-label">姓名：</text>
	          <text class="detail-value">{{ currentDetail.name }}</text>
	        </view>
	        <!-- <view class="detail-item">
	          <text class="detail-label">工号：</text>
	          <text class="detail-value">{{ currentDetail.id }}</text>
	        </view> -->
	        <view class="detail-item">
	          <text class="detail-label">部门：</text>
	          <text class="detail-value">{{ currentDetail.department }}</text>
	        </view>
	        <view class="detail-item">
	          <text class="detail-label">职位：</text>
	          <text class="detail-value">{{ currentDetail.position }}</text>
	        </view>
	        <view class="detail-item">
	          <text class="detail-label">入职日期：</text>
	          <text class="detail-value">{{ formatDate(currentDetail.serviceTime) }}</text>
	        </view>
			<view class="detail-item">
            <text class="detail-label">手机号：</text>
            <text class="detail-value">{{ currentDetail.phone }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">银行卡号：</text>
            <text class="detail-value">{{ currentDetail.bank }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">邮箱：</text>
            <text class="detail-value">{{ currentDetail.email }}</text>
          </view>
	      </view>
	    </uni-popup-dialog>
	  </uni-popup>
  </view>
</template>

<script>
import { globalURL } from '../../constant/config.js'
export default {
  data() {
    return {
      searchName: '',
      employees: [],
      currentEmployee: {},
	  currentDetail: {},
      isNew: true
    }
  },
  watch: {
    searchName(newVal) {
      if (!newVal) this.filteredEmployees = this.employees
    }
  },
  computed: {
    filteredEmployees() {
      return this.employees.filter(item => 
        item.name.toLowerCase().includes(this.searchName.toLowerCase())
		)
    }
  },
  mounted() {
     this.loadData();
   },
  methods: {
	  dateChange(e) {
	    this.currentEmployee.serviceTime = e.detail.value
	  },
	  // 新增日期格式化方法
	    formatDate(dateString) {
	      if (!dateString) return ''
	      const date = new Date(dateString)
	      const year = date.getFullYear()
	      const month = (date.getMonth() + 1).toString().padStart(2, '0')
	      const day = date.getDate().toString().padStart(2, '0')
	      return `${year}-${month}-${day}`
	    },
	  async loadData() {
	       try {
	         const res = await uni.request({
	           url: `${globalURL}/api/fStaff`,
	           method: 'GET'
	         });
			 console.log("GET的数据",res.data.data)
	         this.employees = res.data.data;
	       } catch (e) {
	         uni.showToast({ title: '加载失败', icon: 'error' });
	       }
	     },
	     
	     async saveEmployee() {
	       try {
	         const method = this.isNew ? 'POST' : 'PUT';
	         const url = `${globalURL}/api/fStaff`;
	         
			// 处理日期格式
		    const payload = {
		      ...this.currentEmployee,
		      serviceTime: this.currentEmployee.serviceTime + 'T00:00:00' // 补充时间部分
		    }
			
	         await uni.request({
	           url,
	           method,
	           data: payload
	         });
	         
	         await this.loadData(); // 重新加载数据
	         uni.showToast({ title: '保存成功', icon: 'success' });
	       } catch (e) {
	         uni.showToast({ title: '保存失败', icon: 'error' });
	       }
	       this.$refs.editPopup.close();
	     },
	  	
	     async deleteEmployee(id) {
	       try {
	         await uni.request({
	           url: `${globalURL}/api/fStaff/${id}`,
	           method: 'DELETE'
	         });
	         await this.loadData(); // 重新加载数据
	       } catch (e) {
	         uni.showToast({ title: '删除失败', icon: 'error' });
	       }
	     },
    addEmployee() {
      this.isNew = true
      this.currentEmployee = {
        name: '',
        id: '',
        department: '',
        position: '',
        serviceTime: '',
		phone: '',
        bank: '',
        email: ''
      }
      this.$refs.editPopup.open()
    },
    editEmployee(index) {
      this.isNew = false
      this.currentEmployee = {...this.employees[index]}
      this.$refs.editPopup.open()
    },
	showDetail(item) {
	      this.currentDetail = {
			...item,
		   // 添加格式化后的日期字段
		    formattedServiceTime: this.formatDate(item.serviceTime)}
	      this.$refs.detailPopup.open()
	},
	 
  }
}
</script>

<style scoped>

.empty-tips {
  text-align: center;
  color: #999;
  padding: 20rpx;
}

/* 新增日期样式 */
.date {
  font-size: 24rpx;
  color: #666;
  margin-right: 15rpx;
}

.container {
  padding: 20rpx;
  background-color: #f5f5f5;
}

/* 搜索栏 */
.search-box {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
  padding: 0 20rpx;
}

/* 新增样式 */
.detail-value.email {
  word-break: break-all;
  color: #1890ff;
}

.form-input[type="number"] {
  font-family: Consolas, monospace;
}

.search-input {
  flex: 1;
  height: 70rpx;
  padding: 0 20rpx;
  background: #fff;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.add-btn {
  width: 200rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: #007aff;
  color: white;
  border-radius: 8rpx;
  font-size: 28rpx;
  flex-shrink: 0;
}

/* 员工列表 */
.list-container {
  height: calc(100vh - 160rpx);
  padding: 0 20rpx;
}

.list-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  margin-bottom: 20rpx;
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: #007aff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.info-container {
  flex: 1;
}

.name {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 10rpx;
}

.details {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.details text {
  font-size: 24rpx;
  color: #666;
  margin-right: 15rpx;
}

.separator {
  color: #ddd;
  margin: 0 10rpx;
}

/* 操作按钮 */
.action-container {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
  margin-left: 20rpx;
}

.action-btn {
  width: 120rpx;
  height: 50rpx;
  line-height: 50rpx;
  font-size: 24rpx;
  border-radius: 6rpx;
  padding: 0;
}

.detail { color: #337ab7; background: #eef7ff; }
.edit { color: #337ab7; background: #eef7ff; }
.delete { color: #d9534f; background: #ffeceb; }

/* 表单样式 */
.form-container {
  padding: 20rpx 0;
}

.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.label {
  width: 140rpx;
  font-size: 28rpx;
  color: #666;
  flex-shrink: 0;
}

.form-input {
  flex: 1;
  height: 70rpx;
  padding: 0 20rpx;
  border: 1rpx solid #eee;
  border-radius: 6rpx;
  font-size: 28rpx;
}
/* 新增详情弹窗样式 */
.detail-container {
  padding: 20rpx 30rpx;
  width: 600rpx;
}

.detail-item {
  display: flex;
  align-items: center;
  margin-bottom: 25rpx;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.detail-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.detail-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #666;
  flex-shrink: 0;
}

.detail-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  word-break: break-all;
  padding-left: 20rpx;
}
</style>