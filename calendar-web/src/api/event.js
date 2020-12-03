import { axios } from '@/utils/request'

const api = {
    user: '/userForm',
    role: '/role',
    service: '/service',
    permission: '/permission',
    permissionNoPager: '/permission/no-pager',
    orgTree: '/org/tree'
}

export default api

export function getUserList(parameter) {
    return axios({
        url: api.user,
        method: 'get',
        params: parameter
    })
}

