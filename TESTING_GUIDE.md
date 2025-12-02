# 🧪 Guía de Pruebas - Sistema de Suscripciones

## ✅ Problema Resuelto
Se eliminó el `SubscriptionController` duplicado en `profiles.interfaces.rest`. Ahora solo existe uno en `iam.interfaces.rest` con la ruta `/api/v1/subscriptions`.

---

## 📋 Pre-requisitos

### 1. Base de Datos MySQL
```sql
-- Crear las bases de datos si no existen
CREATE DATABASE IF NOT EXISTS artcollab_auth;
CREATE DATABASE IF NOT EXISTS artcollab_monetization;

-- Verificar tablas en monetization
USE artcollab_monetization;
SHOW TABLES;
-- Deberías ver: payment_preferences, payment_records, activation_queue
```

### 2. Configuración de Mercado Pago
Edita `Backend/monetization-service/src/main/resources/application.properties`:
```properties
mercadopago.access-token=TEST-TU_ACCESS_TOKEN_AQUI
mercadopago.public-key=TEST-TU_PUBLIC_KEY_AQUI
```

**Obtener credenciales de prueba:**
1. Ve a https://www.mercadopago.com.ar/developers/panel
2. Crea una aplicación de prueba
3. Copia el Access Token y Public Key de TEST

---

## 🚀 Paso 1: Iniciar Servicios Backend

Abre 4 terminales diferentes:

### Terminal 1 - Discovery Server
```bash
cd Backend/discovery-server
./mvnw spring-boot:run
```
Espera a ver: `Started DiscoveryServerApplication`

### Terminal 2 - API Gateway
```bash
cd Backend/api-gateway
./mvnw spring-boot:run
```
Espera a ver: `Started ApiGatewayApplication`

### Terminal 3 - Auth Service
```bash
cd Backend/auth-service
./mvnw spring-boot:run
```
Espera a ver: `Started AuthServiceApplication`

### Terminal 4 - Monetization Service
```bash
cd Backend/monetization-service
./mvnw spring-boot:run
```
Espera a ver: `Started MonetizationServiceApplication`

---

## 🌐 Paso 2: Iniciar Frontend

```bash
cd WebFrontend
npm start
# o
ng serve
```

Abre: http://localhost:4200

---

## 🧪 Paso 3: Flujo de Prueba Completo

### 3.1 Registro/Login
1. Regístrate como nuevo usuario o inicia sesión
2. Completa tu perfil (Ilustrador o Escritor)
3. Verifica que NO seas premium (badge "Free")

### 3.2 Ir a Suscripciones
1. Navega a la página de suscripciones
2. Deberías ver el componente de pago de Mercado Pago
3. Precio: $9.99 USD

### 3.3 Crear Preferencia de Pago
1. Click en "Suscribirse" o botón similar
2. El sistema llamará a: `POST /api/mercadopago/preferences`
3. Serás redirigido a Mercado Pago

**Verificar en BD:**
```sql
USE artcollab_monetization;
SELECT * FROM payment_preferences ORDER BY created_at DESC LIMIT 1;
-- Deberías ver tu preferencia guardada
```

### 3.4 Pagar en Mercado Pago (Sandbox)

**Tarjetas de Prueba:**

✅ **Aprobada:**
- Número: `4509 9535 6623 3704`
- CVV: Cualquier 3 dígitos
- Fecha: Cualquier fecha futura
- Nombre: APRO

❌ **Rechazada:**
- Número: `4013 5406 8274 6260`
- CVV: Cualquier 3 dígitos
- Fecha: Cualquier fecha futura
- Nombre: OTHE

### 3.5 Webhook y Activación

Después de pagar, Mercado Pago enviará un webhook a:
```
POST http://localhost:8080/api/mercadopago/webhook
```

**Logs a buscar en Monetization Service:**
```
=== Webhook recibido ===
Type: payment
Payment Status: approved
Payment ID: 123456789
Payer Email: tu_email@test.com
Preference ID: PREF_ID
Processing payment notification
Activating subscription for user: X
```

**Logs a buscar en Auth Service:**
```
Activating subscription for user ID: X
User subscription activated successfully
```

### 3.6 Verificar Activación

**En el Frontend:**
1. Serás redirigido a `/subscription-success`
2. El badge debería cambiar a "Premium" ⭐
3. Recarga la página para confirmar

**En la Base de Datos:**
```sql
-- Ver el pago registrado
USE artcollab_monetization;
SELECT * FROM payment_records ORDER BY payment_date DESC LIMIT 1;

-- Ver el estado del usuario
USE artcollab_auth;
SELECT id, username, email FROM users WHERE email = 'tu_email@test.com';

-- Ver el perfil (Ilustrador)
SELECT user_id, subscripcion FROM ilustradores WHERE user_id = X;

-- O (Escritor)
SELECT user_id, subscripcion FROM escritores WHERE user_id = X;
```

---

## 🔍 Paso 4: Pruebas de API Directas

### Verificar Estado de Suscripción
```bash
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

### Activar Suscripción Manualmente (para pruebas)
```bash
curl -X POST http://localhost:8080/api/v1/subscriptions/activate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "1",
    "userType": "ILUSTRADOR",
    "paymentId": "test-payment-123"
  }'
```

### Desactivar Suscripción
```bash
curl -X POST http://localhost:8080/api/v1/subscriptions/deactivate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "1",
    "userType": "ILUSTRADOR"
  }'
```

---

## 🐛 Troubleshooting

### Problema: Webhook no llega
**Solución:** En desarrollo local, Mercado Pago no puede enviar webhooks. Opciones:
1. Usar ngrok para exponer tu localhost
2. Simular el webhook manualmente con curl
3. Activar la suscripción manualmente con la API

**Simular Webhook:**
```bash
curl -X POST "http://localhost:8080/api/mercadopago/webhook?type=payment&data_id=123456789"
```

### Problema: Suscripción no se activa
**Verificar:**
1. Logs del Monetization Service
2. Tabla `payment_preferences` tiene el userId correcto
3. El email del pago coincide con el email del usuario
4. El Auth Service está corriendo

### Problema: Error 404 en activación
**Verificar:**
1. El usuario existe en la BD
2. El perfil (Ilustrador/Escritor) existe
3. El `userType` es correcto ("ILUSTRADOR" o "ESCRITOR")

---

## ✅ Checklist de Verificación

- [ ] MySQL corriendo
- [ ] 4 servicios backend iniciados
- [ ] Frontend corriendo
- [ ] Credenciales de Mercado Pago configuradas
- [ ] Usuario registrado y con perfil
- [ ] Preferencia de pago creada
- [ ] Pago completado en Mercado Pago
- [ ] Webhook recibido (o simulado)
- [ ] Suscripción activada en BD
- [ ] Badge "Premium" visible en frontend

---

## 📊 Endpoints Importantes

| Servicio | Endpoint | Método | Descripción |
|----------|----------|--------|-------------|
| Monetization | `/api/mercadopago/preferences` | POST | Crear preferencia de pago |
| Monetization | `/api/mercadopago/webhook` | POST | Recibir notificación de pago |
| Auth | `/api/v1/subscriptions/activate` | POST | Activar suscripción |
| Auth | `/api/v1/subscriptions/status/{userId}` | GET | Ver estado de suscripción |
| Auth | `/api/v1/subscriptions/deactivate` | POST | Desactivar suscripción |

---

## 🎯 Casos de Prueba

### Caso 1: Pago Exitoso
1. Usuario sin suscripción
2. Crea preferencia de pago
3. Paga con tarjeta aprobada
4. Webhook activa suscripción
5. ✅ Usuario es premium

### Caso 2: Pago Rechazado
1. Usuario sin suscripción
2. Crea preferencia de pago
3. Paga con tarjeta rechazada
4. Redirigido a `/subscription-failure`
5. ✅ Usuario sigue sin suscripción

### Caso 3: Usuario Ya Premium
1. Usuario con suscripción activa
2. Intenta pagar de nuevo
3. ✅ Debería funcionar (renovación)

### Caso 4: Email Diferente
1. Usuario registrado con email A
2. Paga con email B en Mercado Pago
3. ❌ Validación falla (seguridad)

---

¿Listo para probar? 🚀
