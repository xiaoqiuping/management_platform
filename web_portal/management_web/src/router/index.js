import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

//感觉没用
Router.selfaddRoutes = function (params){
  Router.matcher = new Router().matcher;
  Router.addRoutes(params)
}

/* Layout */
import Layout from '../views/main/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {path: '/login', component: () => import('@/views/login/index'), hidden: true},
  // {path: '/404', component: () => import('@/views/error/404'), hidden: true},
  {path: '/error', component: () => import('@/views/error/error'), hidden: true},
  {
    path: '',
    component: Layout,
    hidden: true
    //redirect: '/home',
    // children: [{
    //   path: 'home',
    //   name: 'home',
    //   component: () => import('@/views/home/index'),
    //   meta: {title: '首页', icon: 'home'}
    // }]
  }
]

export const asyncRouterMap = [
  // 后台管理
  {
    path: '/backstage',
    component: Layout,
    name: 'log',
    meta: {title: '日志', icon: 'log'},
    children: [
      {
        path: 'log',
        name: 'log_list',
        component: () => import('@/views/backstage/log/index'),
        meta: {title: '日志列表', icon: 'log2'}
      }
    ]
  },
  {
    path: '/backstage',
    component: Layout,
    name: 'supplier',
    meta: {title: '供应商', icon: 'supplier'},
    children: [
      {
        path: 'supplier',
        name: 'supplier_list',
        component: () => import('@/views/backstage/supplier/index'),
        meta: {title: '供应商列表', icon: 'supplier'}
      }
    ]
  },
  {
    path: '/backstage',
    component: Layout,
    name: 'process',
    meta: {title: '工序流程管理', icon: 'table'},
    children: [
      {
        path: 'process_list',
        name: 'process_list',
        component: () => import('@/views/backstage/process/index'),
        meta: {title: '工序流程', icon: 'table'}
      }
    ]
  },
  {
    path: '/backstage',
    component: Layout,
    redirect: '/backstage/admin',
    name: 'power',
    meta: {title: '权限', icon: 'backstage'},
    children: [
      {
        path: 'account',
        name: 'account',
        component: () => import('@/views/backstage/admin/index'),
        meta: {title: '用户列表', icon: 'backstage-admin'}
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/views/backstage/role/index'),
        meta: {title: '角色列表', icon: 'backstage-role'}
      },
      {
        path: 'department',
        name: 'department',
        component: () => import('@/views/backstage/department/index'),
        meta: {title: '部门列表', icon: 'backstage-department'}
      },
      {
        path: 'allocMenu',
        name: 'allocMenu',
        component: () => import('@/views/backstage/role/allocMenu'),
        meta: {title: '分配菜单'},
        hidden: true
      },
      {
        path: 'menu',
        name: 'menu',
        component: () => import('@/views/backstage/menu/index'),
        meta: {title: '菜单列表', icon: 'backstage-menu'}
      }
    ]
  },

  //销售管理
  {
    path: '/sale',
    component: Layout,
    name: 'order',
    meta: {title: '订单管理', icon: 'xsdd'},
    children: [
      {
        path: 'sale_order',
        name: 'sale_order',
        component: () => import('@/views/sale/order/order'),
        meta: {title: '销售订单', icon: 'xsdd2'}
      },
      {
        path: 'sale_change',
        name: 'sale_change',
        component: () => import('@/views/sale/order/change'),
        meta: {title: '销售变更单', icon: 'xsdd3'}
      },
      {
        path: 'deliver_notice',
        name: 'deliver_notice',
        component: () => import('@/views/sale/order/deliverNotice'),
        meta: {title: '发货通知单', icon: 'xsdd6'}
      },
      {
        path: 'return_notice',
        name: 'return_notice',
        component: () => import('@/views/sale/order/returnNotice'),
        meta: {title: '退货通知单', icon: 'xsdd5'}
      },
    ]
  },
  {
    path: '/sale_management',
    component: Layout,
    name: 'price',
    meta: {title: '价目表管理', icon: 'jg'},
    children: [
      {
        path: 'sale_price',
        name: 'sale_price',
        component: () => import('@/views/sale/price/salePrice'),
        meta: {title: '销售价目表', icon: 'jgsc'}
      },
      {
        path: 'saleoff_price',
        name: 'saleoff_price',
        component: () => import('@/views/sale/price/saleoffPrice'),
        meta: {title: '销售折扣表', icon: 'jgsc'}
      },
    ]
  },
  {
    path: '/sale_management',
    component: Layout,
    name: 'receivable',
    meta: {title: '应收单管理', icon: 'log2'},
    children: [
      {
        path: 'receivable_list',
        name: 'receivable_list',
        component: () => import('@/views/sale/receivable/index'),
        meta: {title: '应收单列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/sale_management',
    component: Layout,
    name: 'contract',
    meta: {title: '合同管理', icon: 'log'},
    children: [
      {
        path: 'sale_contract',
        name: 'sale_contract',
        component: () => import('@/views/sale/contract/index'),
        meta: {title: '销售合同', icon: 'log2'}
      },
    ]
  },
  {
    path: '/sale_management',
    component: Layout,
    name: 'send_goods_order',
    meta: {title: '发货申请管理', icon: 'fhsq'},
    children: [
      {
        path: 'send_goods_order',
        name: 's_send_goods_order',
        component: () => import('@/views/sale/send_goods_order/index'),
          meta: {title: '发货申请', icon: 'fhsq'}
      },
    ]
  },

  //仓库管理
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'production_order',
    meta: {title: '生产订单管理', icon: 'log'},
    children: [
      {
        path: 'production_order_list',
        name: 'production_order_list',
        component: () => import('@/views/warehouse_storage/production_order/index'),
        meta: {title: '生产订单列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'warehouse_order',
    meta: {title: '入库单管理', icon: 'log'},
    children: [
      {
        path: 'warehouse_order_list',
        name: 'warehouse_order_list',
        component: () => import('@/views/warehouse_storage/warehouse_order/index'),
        meta: {title: '入库单列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'ws_send_goods_order',
    meta: {title: '发货单管理', icon: 'log'},
    children: [
      {
        path: 'send_goods_order',
        name: 'w_send_goods_order',
        component: () => import('@/views/warehouse_storage/send_goods_order/index'),
        meta: {title: '发货单列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'raw_material',
    meta: {title: '原材料管理', icon: 'log'},
    children: [
      {
        path: 'raw_material_list',
        name: 'raw_material_list',
        component: () => import('@/views/warehouse_storage/raw_material/index'),
        meta: {title: '原材料列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'semi_manufactures',
    meta: {title: '半成品管理', icon: 'log'},
    children: [
      {
        path: 'semi_parts',
        name: 'semi_parts',
        component: () => import('@/views/warehouse_storage/semi_manufactures/semi_parts'),
        meta: {title: '半成品零件', icon: 'log2'}
      },
      {
        path: 'semi_movement',
        name: 'semi_movement',
        component: () => import('@/views/warehouse_storage/semi_manufactures/semi_movement'),
        meta: {title: '半成品机芯', icon: 'log2'}
      },
      {
        path: 'semi_watchcase',
        name: 'semi_watchcase',
        component: () => import('@/views/warehouse_storage/semi_manufactures/semi_watchcase'),
        meta: {title: '半成品壳', icon: 'log2'}
      },
    ]
  },
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'manufactures',
    meta: {title: '成品管理', icon: 'log'},
    children: [
      {
        path: 'parts',
        name: 'parts',
        component: () => import('@/views/warehouse_storage/manufactures/parts'),
        meta: {title: '成品零件', icon: 'log2'}
      },
      {
        path: 'movement',
        name: 'movement',
        component: () => import('@/views/warehouse_storage/manufactures/movement'),
        meta: {title: '成品机芯', icon: 'log2'}
      },
      {
        path: 'watchcase',
        name: 'watchcase',
        component: () => import('@/views/warehouse_storage/manufactures/watchcase'),
        meta: {title: '成品壳', icon: 'log2'}
      },
      {
        path: 'watch',
        name: 'watch',
        component: () => import('@/views/warehouse_storage/manufactures/watch'),
        meta: {title: '成品表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/warehouse_storage',
    component: Layout,
    name: 'materiel',
    meta: {title: '物料管理', icon: 'log'},
    children: [
      {
        path: 'material_list',
        name: 'material_list',
        component: () => import('@/views/warehouse_storage/material/list'),
        meta: {title: '总物料列表', icon: 'log2'}
      },
      {
        path: 'expend_record',
        name: 'expend_record',
        component: () => import('@/views/warehouse_storage/material/expend_record'),
        meta: {title: '耗费记录', icon: 'log2'}
      },
      {
        path: 'production_picking',
        name: 'production_picking',
        component: () => import('@/views/warehouse_storage/material/production_picking'),
        meta: {title: '生产领料单', icon: 'log2'}
      },
      {
        path: 'production_return',
        name: 'production_return',
        component: () => import('@/views/warehouse_storage/material/production_return'),
        meta: {title: '生产退料单', icon: 'log2'}
      },
      {
        path: 'production_replenish',
        name: 'production_replenish',
        component: () => import('@/views/warehouse_storage/material/production_replenish'),
        meta: {title: '生产补料单', icon: 'log2'}
      },
    ]
  },

  //原料采购
  {
    path: '/raw_material_purchase',
    component: Layout,
    name: 'purchase_order',
    meta: {title: '原料采购单管理', icon: 'log'},
    children: [
      {
        path: 'purchase_order_list',
        name: 'purchase_order_list',
        component: () => import('@/views/raw_material_purchase/purchase_order/index'),
        meta: {title: '原料采购单', icon: 'log2'}
      },
    ]
  },
  {
    path: '/raw_material_purchase',
    component: Layout,
    name: 'payable_order',
    meta: {title: '应付单管理', icon: 'log'},
    children: [
      {
        path: 'payable_order_list',
        name: 'payable_order_list',
        component: () => import('@/views/raw_material_purchase/payable_order/index'),
        meta: {title: '应付单', icon: 'log2'}
      },
    ]
  },
  {
    path: '/raw_material_purchase',
    component: Layout,
    name: 'purchase_contract',
    meta: {title: '采购单合同管理', icon: 'log'},
    children: [
      {
        path: 'purchase_contract_list',
        name: 'purchase_contract_list',
        component: () => import('@/views/raw_material_purchase/purchase_contract/index'),
        meta: {title: '采购单合同', icon: 'log2'}
      },
    ]
  },
  {
    path: '/raw_material_purchase',
    component: Layout,
    name: 'purchase_price',
    meta: {title: '采购价目管理', icon: 'log'},
    children: [
      {
        path: 'purchase_price_list',
        name: 'purchase_price_list',
        component: () => import('@/views/raw_material_purchase/purchase_price/index'),
        meta: {title: '采购价目', icon: 'log2'}
      },
    ]
  },

  //生产计划
  {
    path: '/production_plan',
    component: Layout,
    name: 'plan_order',
    meta: {title: '计划订单管理', icon: 'log'},
    children: [
      {
        path: 'order_list',
        name: 'order_list',
        component: () => import('@/views/production_plan/plan_order/list'),
        meta: {title: '生产订单管理', icon: 'log2'}
      },
      {
        path: 'supplement_order',
        name: 'supplement_order',
        component: () => import('@/views/production_plan/plan_order/supplement_order'),
        meta: {title: '补增计划单', icon: 'log2'}
      },
    ]
  },
  {
    path: '/production_plan',
    component: Layout,
    name: 'rmp_management',
    meta: {title: '原材料采购单管理', icon: 'log'},
    children: [
      {
        path: 'rmp_list',
        name: 'rmp_list',
        component: () => import('@/views/production_plan/purchase_order/index'),
        meta: {title: '原材料采购单', icon: 'log2'}
      },
    ]
  },
  {
    path: '/production_plan',
    component: Layout,
    name: 'outsource_order',
    meta: {title: '委外单管理', icon: 'log'},
    children: [
      {
        path: 'outsource_order_list',
        name: 'outsource_order_list',
        component: () => import('@/views/production_plan/outsource_order/index'),
        meta: {title: '委外单', icon: 'log2'}
      },
    ]
  },
  //生产管理
  {
    path: '/production_management',
    component: Layout,
    name: 'production_order_management',
    meta: {title: '生产订单管理', icon: 'log'},
    children: [
      {
        path: 'p_order_list',
        name: 'p_order_list',
        component: () => import('@/views/production_management/production_order/index'),
        meta: {title: '生产订单', icon: 'log2'}
      },
    ]
  },
  {
    path: '/production_management',
    component: Layout,
    name: 'payable_order_management',
    meta: {title: '应付单管理', icon: 'log'},
    children: [
      {
        path: 'p_payable_order_list',
        name: 'p_payable_order_list',
        component: () => import('@/views/production_management/payable_order/index'),
        meta: {title: '应付单列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/production_management',
    component: Layout,
    name: 'miscellaneous_material',
    meta: {title: '杂料管理', icon: 'log'},
    children: [
      {
        path: 'mm_picking',
        name: 'mm_picking',
        component: () => import('@/views/production_management/miscellaneous_material/mm_picking'),
        meta: {title: '杂料领料单', icon: 'log2'}
      },
      {
        path: 'mm_return',
        name: 'mm_return',
        component: () => import('@/views/production_management/miscellaneous_material/mm_return'),
        meta: {title: '杂料退料单', icon: 'log2'}
      },
      {
        path: 'mm_replenish',
        name: 'mm_replenish',
        component: () => import('@/views/production_management/miscellaneous_material/mm_replenish'),
        meta: {title: '杂料补料单', icon: 'log2'}
      },
    ]
  },

  //质检
  {
    path: '/quality_restriction',
    component: Layout,
    name: 'raw_material_quality',
    meta: {title: '来料检验管理', icon: 'log'},
    children: [
      {
        path: 'raw_material_quality_list',
        name: 'raw_material_quality_list',
        component: () => import('@/views/quality_restriction/raw_material_quality/index'),
        meta: {title: '来料检验', icon: 'log2'}
      },
    ]
  },
  // {
  //   path: '/quality_restriction',
  //   component: Layout,
  //   name: 'production_quality',
  //   meta: {title: '产品检验管理', icon: 'log'},
  //   children: [
  //     {
  //       path: 'production_quality_list',
  //       name: 'production_quality_list',
  //       component: () => import('@/views/quality_restriction/production_quality/index'),
  //       meta: {title: '产品检验', icon: 'log2'}
  //     },
  //   ]
  // },
  {
    path: '/quality_restriction',
    component: Layout,
    name: 'process_quality',
    meta: {title: '生产检验管理', icon: 'log'},
    children: [
      {
        path: 'process_quality_list',
        name: 'process_quality_list',
        component: () => import('@/views/quality_restriction/process_quality/process'),
        meta: {title: '工序检验', icon: 'log2'}
      },
      {
        path: 'production_quality_list',
        name: 'production_quality_list',
        component: () => import('@/views/quality_restriction/process_quality/production'),
        meta: {title: '成品检验', icon: 'log2'}
      },
    ]
  },
  {
    path: '/quality_restriction',
    component: Layout,
    name: 'send_goods_quality',
    meta: {title: '发货检验管理', icon: 'log'},
    children: [
      {
        path: 'send_goods_quality_list',
        name: 'send_goods_quality_list',
        component: () => import('@/views/quality_restriction/send_goods_quality/index'),
        meta: {title: '发货检验', icon: 'log2'}
      },
    ]
  },
  {
    path: '/quality_restriction',
    component: Layout,
    name: 'refund_goods_quality',
    meta: {title: '退货检验管理', icon: 'log'},
    children: [
      {
        path: 'refund_goods_quality_list',
        name: 'refund_goods_quality_list',
        component: () => import('@/views/quality_restriction/refund_goods_quality/index'),
        meta: {title: '退货检验', icon: 'log2'}
      },
    ]
  },

  //售后管理
  {
    path: '/after_sale_repairing',
    component: Layout,
    name: 'warranty_period_management',
    meta: {title: '保修期管理', icon: 'log'},
    children: [
      {
        path: 'warranty_period_list',
        name: 'warranty_period_list',
        component: () => import('@/views/after_sale_repairing/warranty_period/index'),
        meta: {title: '保修期维修', icon: 'log2'}
      },
    ]
  },
  {
    path: '/after_sale_repairing',
    component: Layout,
    name: 'warranty_maintenance_management',
    meta: {title: '保外维修管理', icon: 'log'},
    children: [
      {
        path: 'warranty_maintenance_list',
        name: 'warranty_maintenance_list',
        component: () => import('@/views/after_sale_repairing/warranty_maintenance/index'),
        meta: {title: '保外维修', icon: 'log2'}
      },
    ]
  },
  {
    path: '/after_sale_repairing',
    component: Layout,
    name: 'asr_receivable',
    meta: {title: '应收单管理', icon: 'log'},
    children: [
      {
        path: 'receivable_list',
        name: 'asr_receivable_list',
        component: () => import('@/views/after_sale_repairing/receivable/index'),
        meta: {title: '应收单列表', icon: 'log2'}
      },
    ]
  },

  //财务管理
  {
    path: '/financial_management',
    component: Layout,
    name: 'report_form_management',
    meta: {title: '报表管理', icon: 'log'},
    children: [
      {
        path: 'finance_report',
        name: 'finance_report',
        component: () => import('@/views/financial_management/report_form/finance_report'),
        meta: {title: '财务报表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/financial_management',
    component: Layout,
    name: 'receivable_management',
    meta: {title: '应收管理', icon: 'log'},
    children: [
      {
        path: 'receivable_list',
        name: 'receivable_list',
        component: () => import('@/views/financial_management/receivable/index'),
        meta: {title: '应收列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/financial_management',
    component: Layout,
    name: 'payable_management',
    meta: {title: '应付管理', icon: 'log'},
    children: [
      {
        path: 'payable_list',
        name: 'payable_list',
        component: () => import('@/views/financial_management/payable/index'),
        meta: {title: '应付列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/financial_management',
    component: Layout,
    name: 'invoice_management',
    meta: {title: '发票管理', icon: 'log'},
    children: [
      {
        path: 'invoice_list',
        name: 'invoice_list',
        component: () => import('@/views/financial_management/invoice/index'),
        meta: {title: '发票列表', icon: 'log2'}
      },
    ]
  },
  {
    path: '/financial_management',
    component: Layout,
    name: 'inventory_account_management',
    meta: {title: '存货核算管理', icon: 'log'},
    children: [
      {
        path: 'inventory_account_list',
        name: 'inventory_account_list',
        component: () => import('@/views/financial_management/inventory_account/index'),
        meta: {title: '存货核算', icon: 'log2'}
      },
    ]
  },
  {
    path: '/financial_management',
    component: Layout,
    name: 'fixed_assets_management',
    meta: {title: '固定资产管理', icon: 'log'},
    children: [
      {
        path: 'fixed_assets_list',
        name: 'fixed_assets_list',
        component: () => import('@/views/financial_management/fixed_assets/index'),
        meta: {title: '固定资产', icon: 'log2'}
      },
    ]
  },
  // {path: '*', redirect: '/404', hidden: true}
]

export default new Router({
  mode: 'hash', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})

