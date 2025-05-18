<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-box">
      <input 
        v-model="searchKey" 
        placeholder="请输入地址或管理员搜索" 
        class="search-input"
        placeholder-style="color:#999"
      />
      <button @tap="addStore" class="add-btn">+ 新增门店</button>
    </view>

	<view v-if="filteredStores.length === 0" class="empty-tips">
	  {{ searchKey ? '未找到相关门店' : '暂无门店数据' }}
	</view>
    <!-- 门店列表 -->
    <scroll-view scroll-y class="list-container">
      <view 
        v-for="(item, index) in filteredStores" 
        :key="index" 
        class="list-item"
      >
        <view class="info-container">
          <text class="name">{{ item.manager }}</text>
          <view class="details">
            <text class="address">{{ item.address }}</text>
            <text class="separator">|</text>
            <text class="phone">{{ item.phone }}</text>
          </view>
        </view>
        <view class="action-container">
          <button @tap="showDetail(item)" class="action-btn detail">门店详情</button>
          <button @tap="editStore(index)" class="action-btn edit">编辑</button>
          <button @tap="deleteStore(index)" class="action-btn delete">删除</button>
        </view>
      </view>
    </scroll-view>

    <!-- 编辑弹窗 -->
    <uni-popup ref="editPopup" type="dialog">
      <uni-popup-dialog 
        :title="isNew ? '新增门店' : '编辑门店'"
        @confirm="saveStore"
      >
        <view class="form-container">
          <view class="form-item">
            <text class="label">门店编号：</text>
            <input v-model="currentStore.id" class="form-input" type="number" disabled/>
          </view>
          <view class="form-item">
            <text class="label">管理员：</text>
            <input v-model="currentStore.manager" class="form-input" />
          </view>
          <view class="form-item">
            <text class="label">地址：</text>
            <input v-model="currentStore.address" class="form-input" />
          </view>
          <view class="form-item">
            <text class="label">电话：</text>
            <input v-model="currentStore.phone" class="form-input" type="tel" />
          </view>
        </view>
      </uni-popup-dialog>
    </uni-popup>

    <!-- 详情弹窗 -->
    <uni-popup ref="detailPopup" type="dialog">
      <uni-popup-dialog title="门店详情" :show-cancel="false" confirm-text="关闭">
        <view class="detail-container">
          <view class="detail-item">
            <text class="detail-label">门店编号：</text>
            <text class="detail-value">{{ currentDetail.id }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">管理员：</text>
            <text class="detail-value">{{ currentDetail.manager }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">地址：</text>
            <text class="detail-value">{{ currentDetail.address }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">电话：</text>
            <text class="detail-value">{{ currentDetail.phone }}</text>
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
        searchKey: '',
        stores: [], // 清空模拟数据
        currentStore: {},
        currentDetail: {},
        isNew: true
      }
    },
    mounted() { // 新增生命周期钩子
      this.loadStores()
    },
	computed:{
		filteredStores() {
		  return this.stores.filter(item => {
		    const key = this.searchKey.toLowerCase()
		    return (
		      item.address.toLowerCase().includes(key) ||
		      item.manager.toLowerCase().includes(key) )
		  })
		}
	},
    methods: {
		addStore() {
		  this.isNew = true
		  this.currentStore = { // 去掉前端生成的ID
		    manager: '',
		    address: '',
		    phone: ''
		  }
		  this.$refs.editPopup.open()
		},
	    editStore(index) {
	      this.isNew = false
	      this.currentStore = {...this.stores[index]}
	      this.$refs.editPopup.open()
	    },
	    showDetail(item) {
	      this.currentDetail = {...item}
	      this.$refs.detailPopup.open()
	    },
      async loadStores() { // 新增数据加载方法
        try {
          const res = await uni.request({
            url: `${globalURL}/api/fStore`,
            method: 'GET'
          })
		  console.log("GET获取数据",res.data.data);
          this.stores = res.data.data
        } catch (e) {
          uni.showToast({ title: '数据加载失败', icon: 'none' })
        }
	 },
	    
	    async saveStore() {
	      try {
	        const method = this.isNew ? 'POST' : 'PUT'
	        await uni.request({
	          url: `${globalURL}/api/fStore`,
	          method: method,
	          data: this.currentStore,
	          header: { 'Content-Type': 'application/json' }
	        })
	        await this.loadStores() // 保存后刷新数据
	        uni.showToast({ title: '保存成功', icon: 'success' })
	        this.$refs.editPopup.close()
	      } catch (e) {
	        uni.showToast({ title: '保存失败', icon: 'none' })
	      }
	    },
	async deleteStore(index) {
	      const id = this.stores[index].id
	      uni.showModal({
	        title: '确认删除',
	        content: '确定要删除该门店记录吗？',
	        success: async (res) => {
	          if (res.confirm) {
	            try {
	              await uni.request({
	                url: `${globalURL}/api/fStore/${id}`,
	                method: 'DELETE'
	              })
	              await this.loadStores() // 删除后刷新数据
	            } catch (e) {
	              uni.showToast({ title: '删除失败', icon: 'none' })
	            }
	          }
	        }
	      })
	    }
	  }
}
</script>

<style scoped>
	
.empty-tips {
  text-align: center;
  padding: 30rpx;
  color: #999;
}

/* 复用员工页面的样式，调整部分颜色标识 */
.detail { color: #337ab7; background: #eef7ff; }
.edit { color: #5cb85c; background: #f0fff0; }
.delete { color: #d9534f; background: #ffeceb; }

/* 地址信息特殊样式 */
.address {
  color: #666;
  font-size: 24rpx;
  max-width: 400rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
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
/* 其他样式与员工管理页面保持一致 */
</style>