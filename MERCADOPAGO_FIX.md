# 🔧 Solución al Error 400 de Mercado Pago

## ❌ Problema
```
POST http://localhost:8080/api/mercadopago/preferences 400 (Bad Request)
Error: "Api error. Check response for details"
```

## ✅ Solución Aplicada

### 1. Configuración Corregida
Se corrigió el archivo `application.properties`:

**Antes (❌ Incorrecto):**
```properties
mercadopago.access.token=TEST-...
mercadopago.public.key=MERCADOPAGO_PUBLIC_KEY=TEST-...  # ❌ Formato incorrecto
```

**Ahora (✅ Correcto):**
```properties
mercadopago.access-token=TEST-6781669663692451-112801-2d02aaec624fcf3a99bd91d20908f6cc-3023132522
mercadopago.public-key=TEST-ebb381fa-759b-459d-9e5d-e5e52e68dfec
```

### 2. Clase de Configuración Creada
Se creó `MercadoPagoConfig.java` que inicializa el SDK automáticamente al arrancar el servicio.

### 3. Mejor Manejo de Errores
Se mejoró el `MercadoPagoController` para mostrar errores detallados en los logs.

---

## 🚀 Pasos para Aplicar la Solución

### Paso 1: Detener el Monetization Service
Si está corriendo, detén el servicio con `Ctrl+C` en la terminal.

### Paso 2: Reiniciar el Monetization Service
```bash
cd Backend/monetization-service
./mvnw spring-boot:run
```

### Paso 3: Verificar en los Logs
Deberías ver este mensaje al iniciar:
```
✅ Mercado Pago SDK configurado correctamente
Access Token: TEST-6781669663692451...
```

### Paso 4: Probar de Nuevo en el Frontend
1. Recarga la página en el navegador
2. Ingresa tu email
3. Click en "Suscribirse"

---

## 🔍 Verificar que Funciona

### En los Logs del Backend
Cuando hagas click en "Suscribirse", deberías ver:

```
=== Creating Mercado Pago Preference ===
User ID: 1
User Type: ILUSTRADOR
Email: erickpalomino0723@gmail.com
Price: 10
Title: Suscripción Premium Mensual - ArtCollab
✅ Preference created successfully!
Preference ID: 3023132522-abc123...
Init Point: https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=...
Sandbox Init Point: https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=...
Payment preference stored in database
```

### Si Sigue Fallando

#### Error: "Invalid credentials"
**Causa:** Las credenciales de Mercado Pago son inválidas o están vencidas.

**Solución:**
1. Ve a https://www.mercadopago.com.ar/developers/panel
2. Crea una nueva aplicación de prueba
3. Copia las credenciales de TEST
4. Actualiza `application.properties`:
```properties
mercadopago.access-token=TU_NUEVO_ACCESS_TOKEN
mercadopago.public-key=TU_NUEVA_PUBLIC_KEY
```
5. Reinicia el servicio

#### Error: "Invalid currency"
**Causa:** La moneda `PEN` (Soles peruanos) no está disponible para tu cuenta.

**Solución:** Edita `MercadoPagoService.java` línea 67:
```java
// Cambiar de:
.currencyId("PEN")

// A tu moneda local:
.currencyId("ARS")  // Argentina
.currencyId("MXN")  // México
.currencyId("COP")  // Colombia
.currencyId("CLP")  // Chile
.currencyId("USD")  // Dólares (si está disponible)
```

#### Error: "Invalid back_urls"
**Causa:** Las URLs de retorno no son válidas.

**Solución:** Verifica que el frontend esté corriendo en `http://localhost:4200`

---

## 🧪 Prueba Manual con cURL

Si quieres probar directamente el endpoint:

```bash
curl -X POST http://localhost:8080/api/mercadopago/preferences \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Subscription",
    "description": "Test payment",
    "price": 10.00,
    "quantity": 1,
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "userId": "1",
    "userType": "ILUSTRADOR"
  }'
```

**Respuesta esperada:**
```json
{
  "preferenceId": "3023132522-abc123...",
  "initPoint": "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=...",
  "sandboxInitPoint": "https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=...",
  "message": "Preferencia creada exitosamente"
}
```

---

## 📋 Checklist

- [ ] Archivo `application.properties` corregido
- [ ] Clase `MercadoPagoConfig.java` creada
- [ ] Monetization Service reiniciado
- [ ] Mensaje "✅ Mercado Pago SDK configurado" visible en logs
- [ ] Prueba desde el frontend exitosa
- [ ] Preferencia creada y guardada en BD

---

## 🆘 Si Nada Funciona

1. **Verifica las credenciales:**
   ```bash
   # En Backend/monetization-service/src/main/resources/application.properties
   cat application.properties | grep mercadopago
   ```

2. **Verifica que el servicio esté corriendo:**
   ```bash
   curl http://localhost:8087/actuator/health
   ```

3. **Revisa los logs completos:**
   - Busca líneas que contengan "ERROR" o "Exception"
   - Copia el stack trace completo

4. **Prueba con credenciales nuevas:**
   - Crea una nueva aplicación en Mercado Pago
   - Usa las credenciales de TEST (no de producción)

---

¿Necesitas ayuda adicional? Comparte los logs del backend cuando intentes crear la preferencia.
