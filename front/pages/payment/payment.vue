<template>
	<view>
		<view class='pay-main'>
			<radio-group @change="radioChange">
				<label class="pay-item" :class="{selected: payType === 'alipay'}">
					<view class="item-content">
						<image class='pay-img' src='../../static/cw_picture/payment/ali_pay.png'></image>
						<view class="pay-info">
							<view class="pay-title">支付宝支付</view>
							<view class='pay-txt'>推荐支付宝用户使用</view>
						</view>
						  <view class="radio-wrapper">
						        <radio value="alipay" color='#FF3333' :checked="payType === 'alipay'"/>
						  </view>
					</view>
				</label>

				<label class="pay-item" :class="{selected: payType === 'wechat'}">
					<view class="item-content">
						<image class='pay-img' src="../../static/cw_picture/payment/wx.jpg"></image>
						<view class="pay-info">
							<view class="pay-title">微信支付</view>
							<view class='pay-txt'>推荐微信用户使用</view>
						</view>
						 <view class="radio-wrapper">
						        <radio value="wechat" color='#FF3333' :checked="payType === 'wechat'"/>
						  </view>
					</view>
				</label>
			</radio-group>
		</view>

		<!-- 去支付 -->
		<view class='pay-foot'>
			<view class='total'>
				<text class="f-color">合计:</text>
				<text class='total-price'>¥{{orderData.amount}}</text>
			</view>
			<view class="go-pay" @tap="handlePayment">去支付</view>
		</view>
	</view>
</template>

<script>
import { globalURL } from '../../constant/config.js'
	export default {
		data() {
			return {
				 orderId: null, // 接收的订单ID
				 payType: 'alipay', // 默认选中支付宝
				 orderData:{
					 amount: 0,
					 orderDetails: [],
					 createTime:'',
				 },
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
			radioChange(e) {
			this.payType = e.detail.value
			},
			handlePayment() {
				console.log('当前支付方式：', this.payType);
				// 这里添加支付逻辑
				if (this.payType === 'alipay'){
					uni.navigateTo({
						url: '/pages/alipay/alipay?orderId='+ this.orderId,
					});
				}else{
					uni.navigateTo({
						url: '/pages/wechat/wechat?orderId='+ this.orderId,
					});
				}
			},
			//获取订单内容
			async loadOrderData(){
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
			       createTime: this.formatDate(apiData.createTime),
			       amount: apiData.orderDetails.reduce((sum, item) => sum + item.price * item.num, 0).toFixed(2)
			     }
			   },
			   formatDate(timestamp) {
				  // 实现日期格式化逻辑
		        const date = new Date(timestamp)
		        return `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`
				},
			goBack(){
				uni.navigateBack({
					delta:1
				})
			}
		}
	}
</script>

<style scoped>
/* 支付项样式 */
.radio-wrapper {
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: translateY(-50%);
}
.pay-main {
	padding: 20rpx 30rpx;
}

.pay-item {
	display: block;
	margin-bottom: 20rpx;
	border-radius: 16rpx;
	background: #fff;
	box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
	transition: all 0.2s;
}

.pay-item.selected {
	border: 2rpx solid #FF3333;
	transform: scale(1.02);
}

.item-content {
	position: relative;
	display: flex;
	align-items: center;
	padding: 30rpx 100rpx 30rpx 30rpx; /* 右侧留出radio空间 */
}

.pay-img {
	width: 80rpx;
	height: 80rpx;
	margin-right: 30rpx;
}

.pay-info {
	flex: 1;
}

.pay-title {
	font-size: 32rpx;
	font-weight: 500;
	color: #333;
}

.pay-txt {
	font-size: 24rpx;
	color: #999;
	margin-top: 8rpx;
}

radio {
  transform: scale(0.9);
}

/* 底部支付栏 */
/* .pay-foot {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	height: 100rpx;
	display: flex;
	align-items: center;
	background: #fff;
	box-shadow: 0 -4rpx 12rpx rgba(0,0,0,0.06);
	padding: 0 30rpx;
} */

/* .total {
	flex: 1;
	font-size: 28rpx;
}
/* 隐藏原生radio，使用自定义样式 */ 

radio .uni-radio-input {
  width: 36rpx;
  height: 36rpx;
  border: 2rpx solid #ccc !important;
  border-radius: 50%;
}

/* 选中状态样式 */
radio .uni-radio-input.uni-radio-input-checked {
  border-color: #FF3333 !important;
  background: #FF3333 url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBmaWxsPSIjZmZmIiBkPSJNOSAxNi4yTDQuOCAxMmwtMS40IDEuNEw5IDE5IDIxIDdsLTEuNC0xLjRMOSAxNi4yeiIvPjwvc3ZnPg==') no-repeat center;
  background-size: 70%;
}
.total-price {
	color: #FF3333;
	font-size: 36rpx;
	font-weight: bold;
	margin-left: 10rpx;
} 

/* .go-pay {
	background: #FF3333;
	color: #fff;
	padding: 20rpx 60rpx;
	border-radius: 50rpx;
	font-size: 32rpx;
	font-weight: 500;
} */
.pay-item{
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding:20rpx;
	border-bottom: 2rpx solid #CCCCCC;
}
.pay-img{
	width: 100rpx;
	height: 100rpx;
}
.pay-txt{
	color:#CCCCCC;
}
.pay-foot{
	width:100%;
	position: fixed;
	left:0;
	bottom:0;
	height: 100rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
}
.total{
	padding-left:100rpx;
	flex:1;
	background-color:#000000;
	line-height: 100rpx;
}
.go-pay{
	color:#FFFFFF;
	background-color: #49BDFB;
	line-height: 100rpx;
	text-align: center;
	width:220rpx;
}
.total-price{
	color:#FFFFFF;
}
</style>
