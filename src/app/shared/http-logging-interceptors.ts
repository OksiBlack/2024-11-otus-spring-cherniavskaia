import {HttpEvent, HttpEventType, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from "@angular/common/http";
import {Observable, tap} from "rxjs";


export const loggerInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
    console.log(`Request is on its way to ${req.url}`);
    return next(req);
}


export function loggingResponseInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    return next(req).pipe(tap(event => {
        if (event.type === HttpEventType.Response) {
            console.log(req.url, 'returned a response with status', event.status, event.body);
        }
    }));
}