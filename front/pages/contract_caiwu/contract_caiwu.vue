<template>
  <view class="container">
    <!-- 合同预览区域 -->
    <view class="contract-box" id="contractCanvas">
      <!-- 合同头部 -->
      <view class="contract-header">
        <text class="contract-title">食品原材料采购合同</text>
        <view class="contract-no">
          <text>合同编号：{{ contractData.contractNo }}</text>
        </view>
      </view>

      <!-- 合同主体 -->
      <view class="contract-body">
        <!-- 签订方信息 -->
        <view class="parties-info">
          <view class="party">
            <text class="party-title">甲方（采购方）：</text>
            <text>{{ contractData.partyA.name }}</text>
            <text>地址：{{ contractData.partyA.address }}</text>
            <text>联系人：{{ contractData.partyA.contact }}</text>
            <text>电话：{{ contractData.partyA.phone }}</text>
          </view>

          <view class="party">
            <text class="party-title">乙方（供应方）：</text>
            <text>{{ contractData.partyB.name }}</text>
            <text>地址：{{ contractData.partyB.address }}</text>
            <text>联系人：{{ contractData.partyB.contact }}</text>
            <text>电话：{{ contractData.partyB.phone }}</text>
          </view>
        </view>

        <!-- 合同条款 -->
        <view class="clause">
          <view class="clause-title">第一条 标的物及数量</view>
          <view class="clause-content">
            <view v-for="(item, index) in contractData.items" :key="index">
              <text>{{ index+1 }}. {{ item.name }}，数量：{{ item.quantity }}吨，单价：¥{{ item.price.toFixed(2) }}</text>
            </view>
          </view>
        </view>

        <view class="clause">
          <view class="clause-title">第二条 合同金额</view>
          <view class="clause-content">
            <text>总金额（大写）：{{ contractData.amountCapital }}</text>
            <text>小写：¥{{ contractData.totalAmount.toFixed(2) }}</text>
          </view>
        </view>

        <!-- 签名区域 -->
        <view class="signature-area">
          <view class="signature-party">
            <text>甲方签字/盖章：</text>
            <image v-if="contractData.signatureA" :src="contractData.signatureA" class="signature-img"></image>
            <text>日期：{{ contractData.signDate }}</text>
          </view>

          <view class="signature-party">
            <text>乙方签字/盖章：</text>
            <image v-if="contractData.signatureB" :src="contractData.signatureB" class="signature-img"></image>
            <text>日期：{{ contractData.signDate }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-btns">
		<button class="save-btn">发给供应商确认</button>
      <!-- <button class="save-btn" @click="saveContract">保存合同</button> -->
      <!-- <button class="sign-btn" @click="handleSign">电子签名</button> -->
    </view>

    <!-- 隐藏的Canvas -->
    <canvas 
      canvas-id="contractCanvas" 
      :style="{ width: canvasWidth + 'px', height: canvasHeight + 'px', position: 'absolute', left: '-9999px' }"
    ></canvas>
  </view>
</template>

<script>
const convertCurrency = require('@/common/currency-convert.js')

export default {
  data() {
    return {
      canvasWidth: 750,
      canvasHeight: 2000,
      contractData: {
        contractNo: 'HT20240521001',
        signDate: '2024-05-21',
        partyA: {
          name: '重交食品工厂',
          address: '重庆市南岸区学府大道66号',
          contact: '王经理',
          phone: '023-12345678'
        },
        partyB: {
          name: '绿色食品供应有限公司',
          address: '重庆市江北区观音桥1号',
          contact: '张经理',
          phone: '023-87654321'
        },
        items: [
          { name: '优质东北大米', quantity: 50, price: 5200.00 },
          { name: '内蒙古牛肉', quantity: 20, price: 6800.00 }
        ],
        totalAmount: 0,
        amountCapital: '',
        signatureA: '/static/cw_picture/contract/signature/A.png',
        signatureB: '/static/cw_picture/contract/signature/B.jpg'
      }
    }
  },
  
  computed: {
    totalAmount() {
      return this.contractData.items.reduce((sum, item) => sum + item.price * item.quantity, 0)
    }
  },

  onLoad(options) {
    this.contractData.totalAmount = this.totalAmount
    this.contractData.amountCapital = convertCurrency(this.totalAmount)
  },

  methods: {
    async saveContract() {
      try {
        const ctx = uni.createCanvasContext('contractCanvas', this)
        const scale = 2
        
        // 设置Canvas尺寸
        this.canvasWidth = 750 * scale
        this.canvasHeight = 2000 * scale
        ctx.scale(scale, scale)

        // 绘制背景
        ctx.setFillStyle('#ffffff')
        ctx.fillRect(0, 0, 750, 2000)

        // 绘制合同标题
        ctx.setFontSize(24)
        ctx.fillText('食品原材料采购合同', 250, 60)

        // 绘制合同编号
        ctx.setFontSize(16)
        ctx.fillText(`合同编号：${this.contractData.contractNo}`, 40, 100)

        // 绘制签订方信息
        this.drawPartyInfo(ctx, 150)

        // 绘制合同条款
        this.drawClauses(ctx, 300)

        // 绘制签名区域
        this.drawSignatures(ctx, 1200)

        ctx.draw(false, async () => {
          const { tempFilePath } = await this.getCanvasTempFile()
          await this.saveImage(tempFilePath)
          uni.showToast({ title: '保存成功' })
        })

      } catch (error) {
        uni.showToast({ title: '生成失败', icon: 'none' })
      }
    },

    drawPartyInfo(ctx, startY) {
      ctx.setFontSize(16)
      // 绘制甲方信息
      ctx.fillText('甲方（采购方）：', 40, startY)
      ctx.fillText(this.contractData.partyA.name, 180, startY)
      let y = startY + 30
      ctx.fillText(`地址：${this.contractData.partyA.address}`, 40, y)
      y += 30
      ctx.fillText(`联系人：${this.contractData.partyA.contact}`, 40, y)
      y += 30
      ctx.fillText(`电话：${this.contractData.partyA.phone}`, 40, y)

      // 绘制乙方信息
      y += 50
      ctx.fillText('乙方（供应方）：', 40, y)
      ctx.fillText(this.contractData.partyB.name, 180, y)
      y += 30
      ctx.fillText('地址：${this.contractData.partyB.address}', 40, y)
      y += 30
      ctx.fillText(`联系人：${this.contractData.partyB.contact}`, 40, y)
      y += 30
      ctx.fillText(`电话：${this.contractData.partyB.phone}`, 40, y)
    },

    drawClauses(ctx, startY) {
      ctx.setFontSize(16)
      let y = startY
      // 第一条
      ctx.fillText('第一条 标的物及数量', 40, y)
      y += 30
      this.contractData.items.forEach((item, index) => {
        ctx.fillText(`${index+1}. ${item.name}，数量：${item.quantity}吨，单价：¥${item.price.toFixed(2)}`, 60, y)
        y += 30
      })

      // 第二条
      y += 50
      ctx.fillText('第二条 合同金额', 40, y)
      y += 30
      ctx.fillText(`总金额（大写）：${this.contractData.amountCapital}`, 60, y)
      y += 30
      ctx.fillText(`小写：¥${this.contractData.totalAmount.toFixed(2)}`, 60, y)
    },

    drawSignatures(ctx, startY) {
      ctx.setFontSize(16)
      // 甲方签名
      ctx.fillText('甲方签字/盖章：', 40, startY)
      ctx.drawImage(this.contractData.signatureA, 180, startY - 30, 150, 150)
      ctx.fillText(`日期：${this.contractData.signDate}`, 40, startY + 100)

      // 乙方签名
      ctx.fillText('乙方签字/盖章：', 400, startY)
      if (this.contractData.signatureB) {
        ctx.drawImage(this.contractData.signatureB, 540, startY - 30, 150, 150)
      }
      ctx.fillText(`日期：${this.contractData.signDate}`, 400, startY + 100)
    },

    handleSign() {
      uni.navigateTo({
        url: '/pages/signature/signature?type=B'
      })
    },

    async getCanvasTempFile() {
      return new Promise((resolve, reject) => {
        uni.canvasToTempFilePath({
          canvasId: 'contractCanvas',
          quality: 1,
          success: resolve,
          fail: reject
        })
      })
    },

    async saveImage(tempFilePath) {
      await uni.authorize({ scope: 'scope.writePhotosAlbum' })
      return new Promise((resolve, reject) => {
        uni.saveImageToPhotosAlbum({
          filePath: tempFilePath,
          success: resolve,
          fail: reject
        })
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  background-color: #f5f5f5;
}

.contract-box {
  background-color: #fff;
  padding: 40rpx;
  border: 2rpx solid #666;
}

.contract-header {
  text-align: center;
  margin-bottom: 40rpx;
}

.contract-title {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
}

.contract-no {
  margin-top: 20rpx;
  font-size: 28rpx;
}

.parties-info {
  margin: 40rpx 0;
}

.party {
  margin-bottom: 40rpx;
}

.party-title {
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.clause {
  margin: 40rpx 0;
}

.clause-title {
  font-weight: bold;
  margin-bottom: 20rpx;
}

.signature-area {
  margin-top: 80rpx;
  display: flex;
  justify-content: space-between;
}

.signature-party {
  width: 45%;
}

.signature-img {
  width: 200rpx;
  height: 200rpx;
  display: block;
  margin: 20rpx 0;
}

.action-btns {
  margin-top: 40rpx;
  display: flex;
  justify-content: space-around;
}

.save-btn {
  background-color: #2E8B57;
  color: white;
  width: 45%;
}

.sign-btn {
  background-color: #007AFF;
  color: white;
  width: 45%;
}
</style>