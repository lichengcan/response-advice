# 使用官方的Java 8镜像作为基础镜像
FROM java

# 设置工作目录
WORKDIR /app

# 将本地构建好的应用程序文件复制到镜像中
COPY target/common-result-0.0.1-SNAPSHOT.jar /app/

# 暴露应用程序运行的端口
EXPOSE 8080

# 启动容器时执行的命令
CMD ["java", "-jar", "/app/common-result-0.0.1-SNAPSHOT.jar"]

#docker build -t docker-test .
#这将会在当前目录中构建一个名为 my-spring-boot-app 的Docker镜像。然后，可以使用以下命令来运行该镜像：


