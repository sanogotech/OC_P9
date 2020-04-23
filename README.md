# MyERP

## Urls
- https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html
- https://stackoverflow.com/questions/2425015/how-to-access-spring-context-in-junit-tests-annotated-with-runwith-and-context
- https://javapapers.com/spring/spring-applicationcontext/

## Test Junit Spring code with  application context bean xml  and @Autowired

 ` ` `java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/services-test-config.xml"})
public class MySericeTest implements ApplicationContextAware
{
  // Injection method 1
  @Autowired
  MyService service;

   //Overrride  AppplicationContextAware Method
    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException
    {
         //Inject method 2
        // Do something with the context here
        HelloWorld hw = (HelloWorld) appContext.getBean("helloWorld");
    }
}

 ` ` `

## Organisation du répertoire

*   `doc` : documentation
*   `docker` : répertoire relatifs aux conteneurs _docker_ utiles pour le projet
    *   `dev` : environnement de développement
*   `src` : code source de l'application


## Environnement de développement

Les composants nécessaires lors du développement sont disponibles via des conteneurs _docker_.
L'environnement de développement est assemblé grâce à _docker-compose_
(cf docker/dev/docker-compose.yml).

Il comporte :

*   une base de données _PostgreSQL_ contenant un jeu de données de démo (`postgresql://127.0.0.1:9032/db_myerp`)


### Lancement

    cd docker/dev
    docker-compose up


### Arrêt

    cd docker/dev
    docker-compose stop


### Remise à zero

    cd docker/dev
    docker-compose stop
    docker-compose rm -v
    docker-compose up
