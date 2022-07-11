export const rules = {
  productionType:[
    { required: true, message: '请选择产品类型', trigger: 'change' }
  ],
  productionName: [
    {required: true, message: '请输入产品名称', trigger: 'blur'},
    {min: 2, max: 100, message: '长度在 2 到 140 个字符', trigger: 'blur'}
  ],




}
