<template>
  <view class="container">
    <!-- 电子发票预览 -->
    <view class="invoice-box" id="invoiceCanvas">
      <!-- 发票头部 -->
      <view class="invoice-header">
        <image src="/static/cw_picture/invoice/票头.jpg" class="invoice-logo"></image>
        <text class="invoice-title">重交食品工厂交易发票</text>
      </view>

      <!-- 发票主体 -->
      <view class="invoice-body">
        <!-- 发票基本信息 -->
        <view class="info-row">
          <text class="label">发票代码：</text>
          <text class="value">144231890154</text>
        </view>
        <view class="info-row">
          <text class="label">发票号码：</text>
          <text class="value">{{ invoiceData.invoiceNo }}</text>
        </view>
        <view class="info-row">
          <text class="label">开票日期：</text>
          <text class="value">{{ invoiceData.createTime }}</text>
        </view>

        <!-- 销售方信息 -->
        <view class="section-title">销售方信息</view>
        <view class="info-row">
          <text class="label">名称：</text>
          <text class="value">{{ invoiceData.supplierName }}</text>
        </view>
        <view class="info-row">
          <text class="label">纳税人识别号：</text>
          <text class="value">{{ invoiceData.taxId }}</text>
        </view>

        <!-- 购买方信息 -->
        <view class="section-title">购买方信息</view>
        <view class="info-row">
          <text class="label">名称：</text>
          <text class="value">{{ invoiceData.buyerName }}</text>
        </view>

        <!-- 商品明细 -->
        <view class="section-title">商品明细</view>
        <view class="goods-header">
          <text class="col-1">名称</text>
          <text class="col-2">数量</text>
          <text class="col-3">单价</text>
          <text class="col-4">金额</text>
        </view>
        <view 
          class="goods-item"
          v-for="(item, index) in invoiceData.details"
          :key="index"
        >
          <text class="col-1">{{ item.name }}</text>
          <text class="col-2">{{ item.quantity }}</text>
          <text class="col-3">{{ item.price.toFixed(2) }}</text>
          <text class="col-4">{{ (item.price * item.quantity).toFixed(2) }}</text>
        </view>

        <!-- 合计信息 -->
        <view class="total-row">
          <text>价税合计（大写）：</text>
          <text>{{ invoiceData.amountCapital }}</text>
        </view>
        <view class="total-row">
          <text>小写：</text>
          <text>¥{{ invoiceData.amount.toFixed(2) }}</text>
        </view>
      </view>

      <!-- 发票脚注 -->
      <view class="invoice-footer">
        <text>开票人：{{ invoiceData.issuer }}</text>
        <text>校验码：{{ invoiceData.checkCode }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-btns">
      <button class="save-btn" @click="saveInvoice">保存电子发票</button>
    </view>

    <!-- 隐藏的Canvas -->
    <canvas 
      canvas-id="invoiceCanvas" 
      :style="{ width: canvasWidth + 'px', height: canvasHeight + 'px', position: 'absolute', left: '-9999px' }"
    ></canvas>
  </view>
</template>

<script>
// 数字金额转中文大写
const convertCurrency = require('@/common/currency-convert.js') 

export default {
  data() {
    return {
      canvasWidth: 750,  // 发票宽度
      canvasHeight: 1200, // 发票高度
      // 模拟发票数据
      invoiceData: {
        orderNo: 'DD20240521001',
        invoiceNo: '144231890154',
        createTime: '2024-05-21 10:30:15',
        supplierName: '重交食品工厂',
        taxId: '91440101MA5C9ABC12',
        buyerName: '粮食供应商',
        details: [
          { name: '牛肉', quantity: 1, price: 5000.00 },
          { name: '大米', quantity: 2, price: 1500.00 }
        ],
        amount: 8000.00,
        amountCapital: '捌仟元整',
        issuer: '张三',
        checkCode: '123456789ABCDEFG'
      }
    }
  },

  onLoad(options) {
    // 从订单详情页获取数据
    if (options.orderId) {
      this.loadInvoiceData(options.orderId)
    } else {
      // 使用模拟数据
      this.invoiceData.amountCapital = convertCurrency(this.invoiceData.amount)
    }
  },
  
  // 在onReady生命周期初始化Canvas
  onReady() {
    this.initCanvasSize()
  },
  
  initCanvasSize() {
    const systemInfo = uni.getSystemInfoSync()
    this.canvasWidth = systemInfo.windowWidth * 2
    this.canvasHeight = systemInfo.windowHeight * 2
  },

  methods: {
    // 加载发票数据（模拟）
    loadInvoiceData(orderId) {
      // 实际开发中应从接口获取数据
      console.log('加载订单数据:', orderId)
      // 保持模拟数据，实际替换为API调用
    },

  //   // 保存发票图片
  //   async saveInvoice() {
  //     try {
  //       // 1. 将HTML内容绘制到Canvas
  //       const ctx = uni.createCanvasContext('invoiceCanvas', this)
        
  //       // 绘制背景
  //       ctx.setFillStyle('#ffffff')
  //       ctx.fillRect(0, 0, this.canvasWidth, this.canvasHeight)
        
  //       // 绘制发票内容（需要根据实际布局调整坐标）
  //       ctx.setFontSize(20)
  //       ctx.fillText('电子增值税普通发票', 280, 60)
        
  //       // ...其他绘制逻辑...

  //       // 2. 绘制完成
  //       ctx.draw(false, () => {
  //         // 3. 生成图片
  //         uni.canvasToTempFilePath({
  //           canvasId: 'invoiceCanvas',
  //           success: res => {
  //             // 4. 保存到相册
  //             uni.saveImageToPhotosAlbum({
  //               filePath: res.tempFilePath,
  //               success: () => {
  //                 uni.showToast({ title: '保存成功' })
  //               },
  //               fail: () => {
  //                 uni.showToast({ title: '保存失败', icon: 'none' })
  //               }
  //             })
  //           }
  //         })
  //       })
  //     } catch (error) {
  //       console.error('保存失败:', error)
  //       uni.showToast({ title: '生成发票失败', icon: 'none' })
  //     }
  //   }
  
  // 在saveInvoice方法中完善Canvas绘制逻辑
  async saveInvoice() {
    try {
      const ctx = uni.createCanvasContext('invoiceCanvas', this)
      const dpr = uni.getSystemInfoSync().pixelRatio
      const scale = 2 // 提高绘制清晰度
  
      // 1. 设置Canvas尺寸（需考虑屏幕像素比）
      this.canvasWidth = 750 * scale
      this.canvasHeight = 1200 * scale
      ctx.scale(scale, scale)
  
      // 2. 绘制背景
      ctx.setFillStyle('#ffffff')
      ctx.fillRect(0, 0, 750, 1200)
  
      // 3. 绘制发票头部
      // 绘制logo（需要先转换为base64或本地路径）
      const logoPath = '/static/cw_picture/invoice/票头.jpg'
      ctx.drawImage(logoPath, 280, 20, 60, 60)
      
      // 绘制标题
      ctx.setFontSize(20)
      ctx.setFillStyle('#000000')
      ctx.fillText('重交食品工厂交易发票', 280, 100)
  
      // 4. 绘制基本信息
      let yPosition = 140
      ctx.setFontSize(14)
      this.drawText(ctx, `发票代码：${this.invoiceData.invoiceNo}`, 40, yPosition)
      yPosition += 25
      this.drawText(ctx, `发票号码：${this.invoiceData.invoiceNo}`, 40, yPosition)
      yPosition += 25
      this.drawText(ctx, `开票日期：${this.invoiceData.createTime}`, 40, yPosition)
      yPosition += 40
  
      // 5. 绘制销售方信息
      this.drawSection(ctx, '销售方信息', yPosition)
      yPosition += 30
      this.drawText(ctx, `名称：${this.invoiceData.supplierName}`, 40, yPosition)
      yPosition += 25
      this.drawText(ctx, `纳税人识别号：${this.invoiceData.taxId}`, 40, yPosition)
      yPosition += 40
  
      // 6. 绘制商品明细表格
      this.drawGoodsTable(ctx, yPosition)
      
      // 7. 绘制完成后生成图片
      ctx.draw(false, async () => {
        const [tempFilePath] = await this.getCanvasTempFilePath()
        await this.saveToAlbum(tempFilePath)
        uni.showToast({ title: '保存成功' })
      })
    } catch (error) {
      console.error('保存失败:', error)
      uni.showToast({ title: '生成发票失败', icon: 'none' })
    }
  },
  
  // 封装绘制方法
  drawText(ctx, text, x, y) {
    ctx.fillText(text, x, y)
  },
  
  // 绘制表格
  drawGoodsTable(ctx, startY) {
    const columnWidth = [300, 100, 100, 150] // 各列宽度
    let y = startY
  
    // 表头
    ctx.setFontSize(14)
    ctx.setFillStyle('#333')
    this.drawText(ctx, '名称', 40, y)
    this.drawText(ctx, '数量', 40 + columnWidth[0], y)
    this.drawText(ctx, '单价', 40 + columnWidth[0] + columnWidth[1], y)
    this.drawText(ctx, '金额', 40 + columnWidth[0] + columnWidth[1] + columnWidth[2], y)
    y += 30
  
    // 表格内容
    ctx.setFontSize(12)
    this.invoiceData.details.forEach(item => {
      this.drawText(ctx, item.name, 40, y)
      this.drawText(ctx, item.quantity.toString(), 40 + columnWidth[0], y)
      this.drawText(ctx, item.price.toFixed(2), 40 + columnWidth[0] + columnWidth[1], y)
      this.drawText(ctx, (item.price * item.quantity).toFixed(2), 
        40 + columnWidth[0] + columnWidth[1] + columnWidth[2], y)
      y += 25
    })
  },
  
  // 获取临时文件路径
  getCanvasTempFilePath() {
    return new Promise((resolve, reject) => {
      uni.canvasToTempFilePath({
        canvasId: 'invoiceCanvas',
        success: resolve,
        fail: reject
      })
    })
  },
  
  // 保存到相册
  async saveToAlbum(tempFilePath) {
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

.invoice-box {
  background-color: #fff;
  padding: 40rpx;
  border: 2rpx solid #666;
}

.invoice-header {
  text-align: center;
  margin-bottom: 40rpx;
}

.invoice-logo {
  width: 120rpx;
  height: 120rpx;
}

.invoice-title {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
  margin-top: 20rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: bold;
  margin: 30rpx 0 15rpx;
  border-bottom: 1rpx solid #666;
}

.info-row {
  display: flex;
  margin: 10rpx 0;
}

.label {
  width: 200rpx;
  color: #666;
}

.goods-header, .goods-item {
  display: flex;
  margin: 15rpx 0;
}

.col-1 { width: 40%; }
.col-2 { width: 15%; }
.col-3 { width: 20%; }
.col-4 { width: 25%; }

.total-row {
  text-align: right;
  margin: 20rpx 0;
  font-weight: bold;
}

.invoice-footer {
  margin-top: 40rpx;
  display: flex;
  flex-direction: column;
}

.save-btn {
  margin-top: 40rpx;
  background-color: #2E8B57;
  color: white;
}
</style>