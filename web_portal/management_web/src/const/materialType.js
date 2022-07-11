export const materialType = [
  {
    value: 1,
    label: "原材料"
  },
  {
    value: 2,
    label: "半成品",
    children: [
      {
        value: 1,
        label: "半成品零件"
      },
      {
        value: 2,
        label: "半成品机芯"
      },
      {
        value: 3,
        label: "半成品壳"
      }
    ]
  },
  {
    value: 3,
    label: "成品",
    children: [
      {
        value: 1,
        label: "成品零件"
      },
      {
        value: 2,
        label: "成品机芯"
      },
      {
        value: 3,
        label: "成品壳"
      },
      {
        value: 4,
        label: "成品表"
      },
    ]
  }
]
