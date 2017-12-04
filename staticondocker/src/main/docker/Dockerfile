FROM httpd:2.4

VOLUME /tmp

RUN apt-get -qq update
RUN apt-get install p7zip p7zip-full

ADD /maven/staticondocker.rar staticcontent.rar

RUN mkdir -p /usr/local/apache2/htdocs/staticondocker
RUN 7z x staticcontent.rar -o/usr/local/apache2/htdocs/staticondocker

EXPOSE 80

CMD ["httpd", "-D", "FOREGROUND"]