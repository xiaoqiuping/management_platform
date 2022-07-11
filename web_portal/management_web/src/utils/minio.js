import request from '@/utils/request'
import axios from 'axios'

export const uploadUrl = "http://localhost:8080/management/minio/upload"

const minio = "/minio"
export function upload(file) {
  return request({
    url: minio + '/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: file
  })
}
export function download(fileData){
  axios.get(fileData.url, {
    responseType: 'blob',
  }).then(response => {
    let url = window.URL.createObjectURL(new Blob([response.data]));
    let link = document.createElement('a');
    let fname = fileData.name;
    link.href = url;
    link.setAttribute('download', fname);
    document.body.appendChild(link);
    link.click();
  });
}
