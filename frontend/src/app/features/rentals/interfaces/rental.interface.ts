export interface Rental {
	id: number,
	name: string,
	surface: number,
	price: number,
	picture: string,
	description: string,
	owner_id: number,
	created_at: Date,
	updated_at: Date
}