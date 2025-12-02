# 🚀 Configuración Completa del Sistema de Suscripciones

## 📋 Requisitos Previos

1. **MercadoPago Account**
   - Crear cuenta en https://www.mercadopago.com
   - Obtener Access Token de prueba
   - Configurar webhook URL

2. **Base de Datos**
   - MySQL 8.0+
   - Ejecutar migraciones

3. **Servicios**
   - Discovery Server (Eureka) - Puerto 8761
   - API Gateway - Puerto 8080
   - Auth Service - Puerto 8081
   - Monetization Service - Puerto 8085

## 🔧 Configuración Paso a Paso

### 1. Configurar MercadoPago Access Token

**Backend/monetization-service/src/main/resources/application.properties**
```properties
# MercadoPago Configuration
mercadopago.access.token=TU_ACCESS_TOKEN_AQUI
```

O usar variable de entorno:
```bash
export MERCADOPAGO_ACCESS_TOKEN=TU_ACCESS_TOKEN_AQUI
```

### 2. Crear Base de Datos

Las tablas se crean automáticamente cuando inicias el servicio gracias a JPA/Hibernate.

Solo necesitas crear la base de datos:
```sql
-- Conectar a MySQL
mysql -u root -p

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS monetization_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**¡Eso es todo!** Las 4 tablas necesarias se crearán automáticamente:
- `payment_preferences`
- `payment_records`
- `activation_queue`
- `subscription_logs`

### 3. Configurar Webhook de MercadoPago

1. Ir a https://www.mercadopago.com/developers/panel/notifications/webhooks
2. Agregar nueva URL de webhook:
   ```
   http://TU_DOMINIO/api/mercadopago/webhook
   ```
3. Para desarrollo local, usar ngrok:
   ```bash
   ngrok http 8080
   # Usar la URL de ngrok: https://xxxxx.ngrok.io/api/mercadopago/webhook
   ```

### 4. Configurar URLs de Retorno

**Backend/monetization-service/.../MercadoPagoService.java**
```java
String baseUrl = "http://localhost:4200"; // Cambiar en producción
```

### 5. Iniciar Servicios en Orden

```bash
# 1. Discovery Server
cd Backend/discovery-server
mvn spring-boot:run

# 2. API Gateway
cd Backend/api-gateway
mvn spring-boot:run

# 3. Auth Service
cd Backend/auth-service
mvn spring-boot:run

# 4. Monetization Service
cd Backend/monetization-service
mvn spring-boot:run

# 5. Frontend
cd WebFrontend
npm start
```

## 🧪 Probar el Flujo

### Tarjetas de Prueba (Perú)

**Tarjeta Aprobada:**
- Número: 5031 7557 3453 0604
- CVV: 123
- Fecha: 11/25

**Tarjeta Rechazada:**
- Número: 5031 4332 1540 6351
- CVV: 123
- Fecha: 11/25

### Flujo de Prueba

1. **Ir a la página de suscripción**
   ```
   http://localhost:4200/subscription/payment
   ```

2. **Ingresar email** (debe coincidir con el email del usuario registrado)

3. **Completar pago en MercadoPago**
   - Usar tarjeta de prueba
   - Completar el checkout

4. **Verificar activación**
   ```
   GET http://localhost:8080/api/v1/subscriptions/status/{userId}
   ```

5. **Ver badge premium** en el perfil del usuario

## 🔒 Seguridad Implementada

### Validación de Email
El sistema valida que el email usado en el pago coincida con el email del usuario:

```java
if (!payerEmail.equalsIgnoreCase(registeredEmail)) {
    // BLOQUEAR activación
    // Crear log de seguridad
    return;
}
```

### Logs de Auditoría
Todos los cambios de suscripción se registran en `subscription_logs`:
- Activaciones exitosas
- Activaciones bloqueadas
- Desactivaciones
- Cambios manuales por admin

## 📊 Monitoreo

### Verificar Logs

**Monetization Service:**
```bash
tail -f Backend/monetization-service/logs/application.log
```

**Buscar activaciones bloqueadas:**
```sql
SELECT * FROM subscription_logs 
WHERE action = 'activation_blocked' 
ORDER BY timestamp DESC;
```

### Endpoints de Verificación

**Estado de suscripción:**
```bash
curl http://localhost:8080/api/v1/subscriptions/status/123
```

**Activar manualmente (admin):**
```bash
curl -X POST http://localhost:8080/api/v1/subscriptions/activate \
  -H "Content-Type: application/json" \
  -d '{"userId":"123","userType":"ILUSTRADOR","paymentId":"test"}'
```

## 🎨 Frontend - Usar Badge Premium

### En cualquier componente:

```typescript
import { PremiumBadgeComponent } from '@shared/components/premium-badge/premium-badge.component';
import { SubscriptionStatusService } from '@shared/services/subscription-status.service';

export class MyComponent implements OnInit {
  isPremium: boolean = false;

  constructor(private subscriptionService: SubscriptionStatusService) {}

  ngOnInit() {
    this.subscriptionService.checkSubscriptionStatus(userId).subscribe(
      isPremium => this.isPremium = isPremium
    );
  }
}
```

```html
<div class="user-profile">
  <h2>{{ userName }}</h2>
  <app-premium-badge [isPremium]="isPremium"></app-premium-badge>
</div>
```

## 🐛 Troubleshooting

### Problema: Webhook no se recibe

**Solución:**
1. Verificar que el webhook esté configurado en MercadoPago
2. Usar ngrok para desarrollo local
3. Verificar logs del monetization-service

### Problema: Suscripción no se activa

**Solución:**
1. Verificar que el email del pago coincida con el email del usuario
2. Revisar logs de `subscription_logs`
3. Verificar que el Auth Service esté corriendo
4. Verificar conectividad entre servicios

### Problema: Badge no aparece

**Solución:**
1. Verificar que el componente esté importado
2. Verificar que el servicio esté inyectado
3. Verificar que el userId sea correcto
4. Verificar respuesta del endpoint de status

## 📚 Documentación Adicional

- [MercadoPago Docs](https://www.mercadopago.com.pe/developers/es/docs)
- [Webhook Testing](https://www.mercadopago.com.pe/developers/es/docs/your-integrations/notifications/webhooks)
- [Test Cards](https://www.mercadopago.com.pe/developers/es/docs/checkout-pro/additional-content/test-cards)

## 🎯 Checklist de Producción

- [ ] Cambiar Access Token a producción
- [ ] Configurar webhook con dominio real
- [ ] Actualizar URLs de retorno
- [ ] Configurar SSL/HTTPS
- [ ] Configurar variables de entorno
- [ ] Configurar backups de base de datos
- [ ] Configurar alertas de seguridad
- [ ] Probar flujo completo en producción
- [ ] Documentar proceso de rollback
