<template>
  <div class="app-container">
    <div class="total-layout">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="receivable" style="width: 60px;height: 60px"></svg-icon>
            <div class="total-title">今日应收总额</div>
            <div class="total-value">￥{{ statisticEntry.receivable_today }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="receivable" style="width: 60px;height: 60px"></svg-icon>
            <div class="total-title">昨日应收总额</div>
            <div class="total-value">￥{{ statisticEntry.receivable_yesterday }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="r7"  style="width: 60px;height: 60px"></svg-icon>
            <div class="total-title">近7天应收总额</div>
            <div class="total-value">￥{{ statisticEntry.receivable_seven }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="total-layout">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="receivable" style="width: 60px;height: 60px"></svg-icon>
            <div class="total-title">今日应付总额</div>
            <div class="total-value">￥{{ statisticEntry.pay_today }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="receivable" style="width: 60px;height: 60px"></svg-icon>
            <div class="total-title">昨日应付总额</div>
            <div class="total-value">￥{{ statisticEntry.pay_yesterday }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="r7" style="width: 60px;height: 60px"></svg-icon>
            <div class="total-title">近7天应付总额</div>
            <div class="total-value">￥{{ statisticEntry.pay_seven }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="statistics-layout">
      <div class="layout-title">应收/应付统计</div>
      <el-row>
        <el-col :span="3">
          <div style="padding: 20px">
            <div>
              <div style="color: #909399;font-size: 14px">应收总数</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">￥{{
                  statisticDateEntry.receivable_total
                }}
              </div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">销售应收总额</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">￥{{
                  statisticDateEntry.receivable_sale
                }}
              </div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">售后应收总额</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">￥{{
                  statisticDateEntry.receivable_sale_after
                }}
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="18">
          <div style="padding: 10px;border-left:1px solid #DCDFE6;border-right:1px solid #DCDFE6">
            <el-date-picker
              style="float: right;z-index: 1"
              size="small"
              v-model="orderCountDate"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @change="handleDateChange"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions">
            </el-date-picker>
            <div>
              <ve-histogram
                :data="chartData"
                :loading="loading"
                :data-empty="dataEmpty"
                :extend="chartExtend"
                :settings="chartSettings"></ve-histogram>
            </div>
          </div>
        </el-col>
        <el-col :span="3">
          <div style="padding: 20px">
            <div>
              <div style="color: #909399;font-size: 14px">应付总数</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">￥{{ statisticDateEntry.payable_total }}</div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">采购应付总数</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">￥{{
                  statisticDateEntry.payable_raw_material
                }}
              </div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">办公耗材应付总额</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">
                ￥{{ statisticDateEntry.payable_miscellaneous_material }}
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {formatDate} from '@/utils/date';
import {statistic, statisticByDate} from '@/api/financial_management/reportForm'

export default {
  name: 'home',
  data() {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            let end = new Date();
            let start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一月',
          onClick(picker) {
            let end = new Date();
            let start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      orderCountDate: '',
      chartSettings: {
        xAxisType: 'time',
        labelMap: {'payable': '应付总额', 'receivable': '应收总额'},
      },
      chartExtend: {
        series(v) {
          v && v.forEach(i => {
            i.barGap = '0%';
            i.barWidth = '20%'
          })
          return v
        }
      },
      chartData: {
        columns: [],
        rows: []
      },
      loading: false,
      dataEmpty: false,
      listLoading: false,
      statisticEntry: {
        pay_seven: 0,
        pay_today: 0,
        pay_yesterday: 0,
        receivable_seven: 0,
        receivable_today: 0,
        receivable_yesterday: 0,
      },
      statisticDateEntry: {
        payable_miscellaneous_material: 0,
        payable_raw_material: 0,
        payable_total: 0,
        receivable_sale: 0,
        receivable_sale_after: 0,
        receivable_total: 0,
        list: []
      }
    }
  },
  created() {
    this.initOrderCountDate();
    this.getData();
    this.statistic();
  },
  methods: {
    handleDateChange() {
      this.getData();
    },
    statistic() {
      this.listLoading = true;
      statistic().then(response => {
        this.listLoading = false;
        this.statisticEntry = response.data;
      });
    },
    initOrderCountDate() {
      let start = new Date();
      let end = new Date();
      start.setTime(start.getTime() - 1000 * 60 * 60 * 24 * 6);
      this.orderCountDate = [formatDate(start, 'yyyy-MM-dd'), formatDate(end, 'yyyy-MM-dd')];
    },
    getData() {
      let start = this.orderCountDate[0];
      let end = this.orderCountDate[1];
      statisticByDate(start, end).then(response => {
        this.listLoading = false;
        this.statisticDateEntry = response.data;
        this.chartData = {
          columns: ['date', 'payable', 'receivable'],
          rows: []
        };
        let len = response.data.list.length;
        for (let i = 0; i < len; i++) {
          let item = response.data.list[i];
          this.chartData.rows.push({
            date: item.date,
            payable: item.payable_total_amount,
            receivable: item.receivable_total_amount,
          });
        }
        this.dataEmpty = false;
        this.loading = false
      });
    }
  }
}
</script>

<style scoped>
.app-container {
  /*margin-top: 40px;*/
  /*margin-left: 120px;*/
  /*margin-right: 120px;*/
}

.address-layout {
}

.total-layout {
  margin-top: 20px;
}

.total-frame {
  border: 1px solid #DCDFE6;
  padding: 20px;
  height: 100px;
}

.total-icon {
  color: #409EFF;
  width: 60px;
  height: 60px;
}

.total-title {
  position: relative;
  font-size: 16px;
  color: #909399;
  left: 70px;
  top: -50px;
}

.total-value {
  position: relative;
  font-size: 18px;
  color: #606266;
  left: 70px;
  top: -40px;
}

.un-handle-layout {
  margin-top: 20px;
  border: 1px solid #DCDFE6;
}

.layout-title {
  color: #606266;
  padding: 15px 20px;
  background: #F2F6FC;
  font-weight: bold;
}

.un-handle-content {
  padding: 20px 40px;
}

.un-handle-item {
  border-bottom: 1px solid #EBEEF5;
  padding: 10px;
}

.overview-layout {
  margin-top: 20px;
}

.overview-item-value {
  font-size: 24px;
  text-align: center;
}

.overview-item-title {
  margin-top: 10px;
  text-align: center;
}

.out-border {
  border: 1px solid #DCDFE6;
}

.statistics-layout {
  margin-top: 20px;
  border: 1px solid #DCDFE6;
}

.mine-layout {
  position: absolute;
  right: 140px;
  top: 107px;
  width: 250px;
  height: 235px;
}

.address-content {
  padding: 20px;
  font-size: 18px
}
</style>
