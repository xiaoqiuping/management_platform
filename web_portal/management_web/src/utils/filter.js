import Vue from 'vue'
import {formatDate} from '@/utils/date'

Vue.filter('sexFilter', function (val) {
  if (val === 1) {
    return "男"
  } else if (val === 0) {
    return "女"
  } else {
    return "未知"
  }
})

Vue.filter('enableFilter', function (val) {
  if (val === 0) {
    return "启用"
  } else {
    return "禁用"
  }
})

Vue.filter('formatDate', function (time) {
  if (time == null || time === '') {
    return '';
  }
  let date = new Date(time);
  return formatDate(date, 'yyyy-MM-dd')
})

Vue.filter('formatDateTime', function (time) {
  if (time == null || time === '') {
    return '';
  }
  let date = new Date(time);
  return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
})
