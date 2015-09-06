# Skim.py


### Installation





```sh
git clone https://github.com/GruppoPBDMNG-5/Skim.py
cd  Skim.py
docker build -t gruppo_pbdmng-5/skimpy . 
docker run -d -p 8080:8080 -p 27017:27017 --name=skimpy gruppo_pbdmng-5/skimpy
docker exec -it skimpy bash
./start
```

### Port Forwarding
Host IP-----Port-----Guest IP----Port 
  
127.0.0.1--8080----0.0.0.0-----8080

### Run Service
* Go [localhost:8080](localhost:8080)
* Fun


