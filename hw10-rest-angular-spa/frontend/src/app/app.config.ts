import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {loggerInterceptor, loggingResponseInterceptor} from './shared/http-logging-interceptors';
import {provideHttpClient, withInterceptors, withInterceptorsFromDi} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {provideNativeDateAdapter} from "@angular/material/core";


export const appConfig: ApplicationConfig = {
    providers: [
        provideRouter(routes),
        provideHttpClient(
            withInterceptorsFromDi(),
            withInterceptors([loggerInterceptor, loggingResponseInterceptor])
        ),
        provideAnimationsAsync(),
        provideNativeDateAdapter(),
    ]
};
