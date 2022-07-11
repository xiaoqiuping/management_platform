FROM  docker.io/nginx
LABEL maintainer="liubity <330450826@qq.com>"

EXPOSE 80
COPY ./dist /usr/share/nginx/html/
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
