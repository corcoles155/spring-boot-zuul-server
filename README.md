# spring-boot-zuul-server

Zuul API Gateway ofrece:
- Enrutamiento dinámico
- Balanceo de carga
- Conjunto de filtros para manejar tolerancia a fallos, seguridad, monitorización de métricas...

Tipos de filtros que ofrece Zuul:
- Pre, se ejecuta antes de que se resuelve la ruta, se usa para pasar datos a la request.
- Post, se ejecuta después de que se resuelva la ruta, se usa para modificar la response.
- Rout, se ejecuta durante el enrutado, se usa para la comunicación con el microservicio.
