<template>
	<view>
		<uni-row>
			<uni-col :span="12">
				<view style="margin: 10px;">
					<uni-combox :candidates="product_cate" placeholder="请选择产品类别" v-model="cate"></uni-combox>
				</view>
			</uni-col>
			<uni-col :span="12">
				<uni-search-bar placeholder="输入产品名称" clearButton="auto" cancelButton="none" @confirm="search" />
			</uni-col>
		</uni-row>
		
		<view style="margin-top: 30px;">
			<scroll-view scroll-y="true" style="height: 80vh;">
				<uni-card v-for="(item,index) in productList" :key="index">
					<uni-row>
						<uni-col :span="8">
							<image style="width: 50px;height: 50px;" :src="item.iamge"></image>
						</uni-col>
						<uni-col :span="14">
							<text>{{ item.pName }}</text>
						</uni-col>
						<uni-col :span="2">
							<image style="width: 20px; height: 20px;" src="/static/home/bom/editBom.png" @click="editBom(item.id)"></image>
						</uni-col>
					</uni-row>
				</uni-card>
			</scroll-view>
		</view>
		
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="popup" background-color="#fff">
				<uni-tag style="margin-left: 30px;">修改前请确认信息！</uni-tag>
				<scroll-view scroll-y="true" style="height: 40vh;">
					<view  v-for="(bomItem,index) in productBom" :key="index">
						<uni-card>
							<uni-row>
								<uni-col :span="8">
									<input adjust-position="true" v-model="bomItem.rowName"></input>
								</uni-col>
								<uni-col :span="6" offset="2">
									<input adjust-position="true" type="number" v-model="bomItem.num"></input>
								</uni-col>
								<uni-col :span="4" offset="4">
									<input adjust-position="true" v-model="bomItem.unit"></input>
								</uni-col>
							</uni-row>
						</uni-card>
					</view>
				</scroll-view>
			</uni-popup>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				product_cate: ['面食', '汤食', '肉类', '臊子', '臊了'],
				cate: '',
				productList: [
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '001'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '002'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '003'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '004'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '005'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '006'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '007'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '008'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '009'},
					{ iamge: '/static/home/produce_m.png', pName:'牛肉', id: '011'},
				],
				productBom: [
					{ rowName: '牛肉', num: '10', unit: '千克'},
					{ rowName: '鸭肉', num: '10', unit: '千克'},
					{ rowName: '酱油', num: '120', unit: '克'},
					{ rowName: '白菜', num: '10', unit: '克'},
					{ rowName: '胡萝卜', num: '10', unit: '千克'},
				]
			}
		},
		methods: {
			search(res) {
				uni.showToast({
					title: '搜索：' + res.value,
					icon: 'none'
				})
			},
			// 编辑码表
			editBom(productId) {
				this.$refs.popup.open('bottom')
			},
		}
	}
</script>

<style>

</style>
