<template>
	<view>
		<uni-card title="供应商信息" class="custom-card" :is-shadow="false">
			<uni-row>
				<uni-col 
					  v-for="(item, index) in dataList" 
				      :key="item.id"
				      :span="gridConfig.span"
				      :xs="gridConfig.xs"
				      :sm="gridConfig.sm"
				      :md="gridConfig.md"
				      :lg="gridConfig.lg"
				      class="grid-item">
					    <!-- 卡片容器 -->
					    <view class="card" @click="handleClick(item)">
					        
					        <!-- 图片展示 -->
					        <image 
					          :src="item.image" 
					          mode="aspectFill" 
					          class="card-image"
					          :lazy-load="true"
					          @load="handleImageLoad(index)"
					        />
					        
					        <!-- 内容区 -->
					        <view class="card-content">
					          <text class="title">{{ item.title }}</text>
					          <text class="description">{{ item.desc }}</text>
					        </view>
					        
					        <!-- 角标 -->
					        <view v-if="item.badge" class="badge">{{ item.badge }}</view>
					    </view>
				</uni-col>
				<!-- 空状态提示 -->
				<view v-if="dataList.length === 0" class="empty-tips">
				  <uni-icons type="info" size="48" color="#999" />
				  <text>暂无数据</text>
				</view>
			</uni-row>
		</uni-card>
		<uni-card title="采购管理">
			<text>这是一个通栏卡片 ，通栏没有外边距，左右会贴合父元素。</text>
		</uni-card>
		<uni-card title="合同与发票管理">
			<text>这是一个通栏卡片 ，通栏没有外边距，左右会贴合父元素。</text>
		</uni-card>
		<uni-card title="物流管理">
			<text>这是一个通栏卡片 ，通栏没有外边距，左右会贴合父元素。</text>
		</uni-card>
		<uni-card title="投诉与处罚">
			<text>这是一个通栏卡片 ，通栏没有外边距，左右会贴合父元素。</text>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// 响应式布局配置
				gridConfig: {
					span: 6,   // 默认4列（24/6）
					xs: 6,    // 手机端3列
					sm: 8,     // 平板端3列
					md: 6,     // 桌面端4列
					lg: 4      // 大屏6列
				},
				// 示例数据
				dataList: [
				  {
					id: 1,
					title: '基本信息',
					image: '/static/home/supplier/供应商基本信息管理.png',
				  },
				  {
					id: 2,
					title: '登录信息',
					image: '/static/home/supplier/登录信息.png',
				  },
				  {
					id: 3,
					title: '收款信息',
					image: '/static/home/supplier/收款信息.png',
				  },
				]
			}
		},
		methods: {
			// 图片加载完成回调
			handleImageLoad(index) {
			  this.$set(this.dataList, index, {
				...this.dataList[index],
				loaded: true
			  })
			},
			// 点击事件处理
			handleClick(item) {
			  uni.navigateTo({
				url: `/pages/detail?id=${item.id}`
			  })
			}
		}
	}
</script>

<style lang="scss">
	.grid-item {
	    margin-bottom: 20rpx;
		align-items: center;
	}
	.card{
		align-items: center;
	}
	.card-content {
	    vertical-align: middle; /* 图片垂直居中对齐 */
	    font-size: 12px; /* 缩小文字大小 */
		margin-left: 5px;
	}
	.card-image{
		vertical-align: middle; /* 图片垂直居中对齐 */
		width: 60px;
		height: 60px;
	}
	.custom-card {
	  border: 1px solid #5568e0; /* 添加边框 */
	  border-radius: 4px; /* 添加圆角 */
	}
</style>
