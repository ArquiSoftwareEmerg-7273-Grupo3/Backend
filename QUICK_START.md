# ⚡ Quick Start - Sistema de Suscripciones

## 🚀 Inicio Rápido (5 minutos)

### 1. Configurar MercadoPago Access Token

```bash
# En Backend/monetization-service/src/main/resources/application.properties
mercadopago.access.token=TEST-XXXXXXXX-XXXXXXXX
```

### 2. Iniciar Servicios

```bash
# Terminal 1 - Discovery Server
cd Backend/discovery-server && mvn spring-boot:run

# Terminal 2 - API Gateway
cd Backend/api-gateway && mvn spring-boot:run

# Terminal 3 - Auth Service
cd Backend/auth-service && mvn spring-boot:run

# Terminal 4 - Monetization Service
cd Backend/monetization-service && mvn spring-boot:run

# Terminal 5 - Frontend
cd WebFrontend && npm start
```

### 3. Probar el Flujo

1. **Ir a**: http://localhost:4200/subscription/payment
2. **Ingresar email**: (el mismo email de tu usuario registrado)
3. **Pagar con tarjeta de prueba**:
   - Número: `5031 7557 3453 0604`
   - CVV: `123`
   - Fecha: `11/25`
4. **Ver badge premium** en tu perfil

---

## 🔧 Configuración de Webhook (Desarrollo Local)

### Opción 1: ngrok (Recomendado)

```bash
# Instalar ngrok
brew install ngrok  # Mac
# o descargar de https://ngrok.com

# Exponer puerto 8080
ngrok http 8080

# Copiar la URL HTTPS generada
# Ejemplo: https://abc123.ngrok.io

# Configurar en MercadoPago:
# https://abc123.ngrok.io/api/mercadopago/webhook
```

### Opción 2: Sin Webhook (Testing Manual)

Puedes activar manualmente la suscripción:

```bash
curl -X POST http://localhost:8080/api/v1/subscriptions/activate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "1",
    "userType": "ILUSTRADOR",
    "paymentId": "test-payment-123"
  }'
```

---

## ✅ Verificar que Todo Funciona

### 1. Verificar Servicios

```bash
# Discovery Server
curl http://localhost:8761

# API Gateway
curl http://localhost:8080/actuator/health

# Auth Service
curl http://localhost:8080/api/v1/users

# Monetization Service
curl http://localhost:8080/api/mercadopago/preferences
```

### 2. Verificar Base de Datos

```sql
-- Conectar a MySQL
mysql -u root -p

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS monetization_db;

-- Verificar tablas (después de iniciar el servicio)
USE monetization_db;
SHOW TABLES;

-- Debería mostrar (se crean automáticamente):
-- payment_preferences
-- payment_records
-- activation_queue
-- subscription_logs
-- user_subscriptions
```

### 3. Verificar Estado de Suscripción

```bash
# Reemplazar {userId} con tu ID de usuario
curl http://localhost:8080/api/v1/subscriptions/status/1
```

Respuesta esperada:
```json
{
  "userId": 1,
  "hasActiveSubscription": true,
  "subscriptionActive": true,
  "userType": "ILUSTRADOR"
}
```

---

## 🎨 Usar Badge Premium en tu Componente

### Paso 1: Importar

```typescript
import { PremiumBadgeComponent } from '@shared/components/premium-badge/premium-badge.component';
import { SubscriptionStatusService } from '@shared/services/subscription-status.service';

@Component({
  imports: [PremiumBadgeComponent]
})
```

### Paso 2: Verificar Suscripción

```typescript
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

### Paso 3: Mostrar Badge

```html
<div class="user-header">
  <h2>{{ userName }}</h2>
  <app-premium-badge [isPremium]="isPremium"></app-premium-badge>
</div>
```

---

## 🐛 Problemas Comunes

### Problema: "Access Token inválido"

**Solución**: Verificar que el token esté configurado correctamente en `application.properties`

### Problema: "Webhook no se recibe"

**Solución**: 
1. Usar ngrok para exponer localhost
2. Configurar la URL en MercadoPago
3. Verificar logs del monetization-service

### Problema: "Suscripción no se activa"

**Solución**:
1. Verificar que el email del pago coincida con el email del usuario
2. Revisar logs: `tail -f Backend/monetization-service/logs/application.log`
3. Verificar tabla `subscription_logs` en la base de datos

### Problema: "Badge no aparece"

**Solución**:
1. Verificar que el componente esté importado
2. Verificar que el userId sea correcto
3. Verificar respuesta del endpoint: `curl http://localhost:8080/api/v1/subscriptions/status/{userId}`

---

## 📚 Documentación Completa

- **Setup Completo**: `SUBSCRIPTION_SETUP.md`
- **Resumen de Implementación**: `SUBSCRIPTION_IMPLEMENTATION_SUMMARY.md`
- **Documentación de Badge**: `WebFrontend/src/app/shared/components/premium-badge/README.md`

---

## 🎯 Checklist de Verificación

- [ ] Servicios iniciados (Discovery, Gateway, Auth, Monetization)
- [ ] Frontend corriendo en http://localhost:4200
- [ ] Access Token de MercadoPago configurado
- [ ] Tablas de base de datos creadas
- [ ] Webhook configurado (o activación manual lista)
- [ ] Tarjeta de prueba funcionando
- [ ] Badge premium visible en UI

---

## 💡 Tips

1. **Desarrollo**: Usa activación manual para no depender del webhook
2. **Testing**: Siempre usa tarjetas de prueba de MercadoPago
3. **Logs**: Mantén abiertos los logs del monetization-service
4. **Cache**: Limpia el cache del navegador si el badge no se actualiza

---

**¿Listo para empezar?** 🚀

1. Configura el Access Token
2. Inicia los servicios
3. Prueba el flujo de pago
4. ¡Disfruta de tu sistema de suscripciones!
