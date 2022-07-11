import request from '@/utils/request'

const exportXml = "/exportXml"

export function exportXmlSaleOrder(param) {
  return request({
      url: exportXml + '/saleOrder',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}
export function exportXmlProductionOrder(param) {
  return request({
      url: exportXml + '/productionOrder',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}
export function exportXmlPurchaseOrder(param) {
  return request({
      url: exportXml + '/purchaseOrder',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}
export function exportXmlOutSourceOrder(param) {
  return request({
      url: exportXml + '/outSourceOrder',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}
export function exportXmlWarehouseOrder(param) {
  return request({
      url: exportXml + '/warehouseOrder',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}
export function exportXmlRepairOrder(param) {
  return request({
      url: exportXml + '/repairOrder',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}

export function exportXmlMaterialSummary(param) {
  return request({
      url: exportXml + '/materialSummary',
      method: 'post',
      data: {param},
      responseType: 'blob',
    }
  )
}
