import request from './request'

// 用户登录
export function login(data) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  })
}

// 用户注册
export function register(data) {
  return request({
    url: '/users/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/users/profile',
    method: 'get'
  })
}

// 更新用户信息
export function updateUserInfo(data) {
  return request({
    url: '/users/profile',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/users/change-password',
    method: 'put',
    data
  })
}

// 检查用户名是否存在
export function checkUsername(username) {
  return request({
    url: '/users/check-username',
    method: 'get',
    params: { username }
  })
}

// 检查邮箱是否存在
export function checkEmail(email) {
  return request({
    url: '/users/check-email',
    method: 'get',
    params: { email }
  })
} 