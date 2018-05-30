import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { BillDetailsComponent } from './bill-details.component';
import { BillDetailsDetailComponent } from './bill-details-detail.component';
import { BillDetailsPopupComponent } from './bill-details-dialog.component';
import { BillDetailsDeletePopupComponent } from './bill-details-delete-dialog.component';

export const billDetailsRoute: Routes = [
    {
        path: 'bill-details',
        component: BillDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.billDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bill-details/:id',
        component: BillDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.billDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billDetailsPopupRoute: Routes = [
    {
        path: 'bill-details-new',
        component: BillDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.billDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bill-details/:id/edit',
        component: BillDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.billDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bill-details/:id/delete',
        component: BillDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'billDetailsApp.billDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
