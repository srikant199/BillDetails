import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { LineItomsComponent } from './line-itoms.component';
import { LineItomsDetailComponent } from './line-itoms-detail.component';
import { LineItomsPopupComponent } from './line-itoms-dialog.component';
import { LineItomsDeletePopupComponent } from './line-itoms-delete-dialog.component';

export const lineItomsRoute: Routes = [
    {
        path: 'line-itoms',
        component: LineItomsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.lineItoms.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'line-itoms/:id',
        component: LineItomsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.lineItoms.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lineItomsPopupRoute: Routes = [
    {
        path: 'line-itoms-new',
        component: LineItomsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.lineItoms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'line-itoms/:id/edit',
        component: LineItomsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.lineItoms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'line-itoms/:id/delete',
        component: LineItomsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.lineItoms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
