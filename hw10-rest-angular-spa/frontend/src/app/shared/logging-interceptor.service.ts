import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})export class LoggingInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, handler: HttpHandler): Observable<HttpEvent<any>> {
        console.log('Request URL zzzzzzz: ' + req.url);
        return handler.handle(req);
    }
}