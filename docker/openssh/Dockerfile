FROM lscr.io/linuxserver/openssh-server:latest

ADD ssh /var/keys/ssh

ENV SUDO_ACCESS=true
ENV PASSWORD_ACCESS=true

ENV USER_NAME=black
ENV USER_PASSWORD=radish
ENV PUBLIC_KEY_DIR=/var/keys/ssh
