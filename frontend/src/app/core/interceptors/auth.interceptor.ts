import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { AuthService } from "@auth/services/auth.service";
import { inject } from "@angular/core";
import { Observable } from "rxjs";

export function authInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  const token = inject(AuthService).token();

  // Solo agregar el header Authorization si hay un token válido
  if (token && token.trim().length > 0) {
    const newReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(newReq);
  }

  return next(req);
}
