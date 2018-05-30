import { BaseEntity } from './../../shared';

export class LineItoms implements BaseEntity {
    constructor(
        public id?: number,
        public serialNumber?: number,
        public particular?: string,
        public amounts?: number,
        public billDetailsId?: number,
    ) {
    }
}
