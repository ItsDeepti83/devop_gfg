# mininmal base image (os + python)
#FROM python:latest 
FROM python:3.12.0b3-alpine3.18
# copy all files to /application folder inside the container
COPY . /application
WORKDIR /application
# COPY requirements.txt .
RUN pip install -r requirements.txt
EXPOSE 8080
CMD ["python", "app.py"]