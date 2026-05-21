### Health Check

Este endpoint verifica el estado de la API y sus dependencias.

#### 🔹 Descripción

-   Retorna el estado general de la API.
-   Puede ser utilizado por herramientas de monitoreo y CI/CD.
-   Indica la disponibilidad de servicios clave como bases de datos, caché, etc.

#### 🔹 Ejemplo de respuesta:

```json
{
  "path": "/actuator/health",
  "statusCode": 200,
  "data": {
    "status": "ok",
    "info": {
      "database": {
        "status": "up"
      },
      "cache": {
        "status": "up"
      },
      "externalService": {
        "status": "up"
      }
    },
    "details": {
      "database": {
        "status": "up",
        "details": {
          "database": "PostgreSQL",
          "result": "Connection OK"
        }
      },
      "cache": {
        "status": "up",
        "details": {
          "cache": "Redis",
          "result": "Connection OK"
        }
      },
      "externalService": {
        "status": "up",
        "details": {
          "service": "PokemonClient",
          "result": "Connection OK"
        }
      }
    }
  },
  "error": {}
}
```

