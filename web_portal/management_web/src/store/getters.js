const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  top_menus: state => state.user.top_menus,
  top_menu_index: state => state.user.top_menu_index,
  sider_menus: state => state.user.sider_menus,
  addRouters: state => state.permission.addRouters,
  routers: state => state.permission.routers
}
export default getters
