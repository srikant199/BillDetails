import { BaseEntity } from './../../shared';

export class BillDetails implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public name?: string,
        public billNumber?: number,
        public phoneNumber?: string,
        public lineItoms?: BaseEntity[],
    ) {
    }
}
