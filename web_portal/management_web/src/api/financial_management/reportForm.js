import request from '@/utils/request'

const report_form="/report_form"

export function statistic(){
  return request({
    url: report_form + '/statistic',
    method: 'post',
  })
}
export function statisticByDate(start_date,end_date){
  return request({
    url: report_form + '/statisticByDate',
    method: 'post',
    params:{start_date,end_date}
  })
}
