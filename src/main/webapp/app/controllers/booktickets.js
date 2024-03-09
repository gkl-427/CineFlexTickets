import Ember from 'ember';
import ENV from '../config/environment';

export default Ember.Controller.extend({
    totalPrice: 0,
    selectedSeats: [],
    show_id: null,
    status: false,
    payment_mode: null,
    seat_ids: [],
    seatnumber: [],
    renderedSeats: [],
    currentUser: Ember.inject.service(),
    paymentmode: ['', 'UPI', 'Net Banking', 'Card'],
    currentUser: Ember.inject.service(),
    isAuthenticate: Ember.computed('currentUser.isAuthenticated', function () {
        return this.get('currentUser').get('isAuthenticated');
    }),
    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') === 2;
    }),

    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),

    actions: {
        bookTickets() {
            if (this.selectedSeats.length > 0) {
                for (let i = 0; i < this.selectedSeats.length; i++) {
                    this.totalPrice += this.selectedSeats[i].ticketprice;
                    this.set("status", true);
                }
            }
            else {
                alert("No seats selected");
            }
        },

        toggleCheckbox(seat) {
            var temp = seat.isSelected;
            //this.toggleProperty('isSelected');
            Ember.set(seat, 'isSelected', !temp);
            if (seat.seatAvailable) {
                if (seat.isSelected) {
                    this.selectedSeats.push(seat);
                    this.seatnumber.push(seat.seatrow + '' + seat.seatnumber);
                } else {
                    const index = this.selectedSeats.indexOf(seat);
                    if (index !== -1) {
                        this.selectedSeats.splice(index, 1);
                        this.seatnumber.splice(index, 1);
                    }
                }
            }
        },

        selectPaymentmode(Value) {
            this.set('payment_mode', Value);
        },

        book() {
            for (let i = 0; i < this.selectedSeats.length; i++) {
                this.seat_ids.push(this.selectedSeats[i].seatid);
            }
            function removeDuplicates(seat_ids) {
                return [...new Set(seat_ids)];
            }

            let formData = {
                showid: this.get('show_id'),
                paymentmode: this.get('payment_mode'),
                paymentstatus: true,
                amount: this.get('totalPrice'),
                paymenttime: new Date().getTime(),
                seatid: JSON.stringify(removeDuplicates(this.seat_ids)),
                userid: this.get('currentUser').userId,
                seatnumber: JSON.stringify(this.get('seatnumber')),
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/bookings?addentry',
                type: 'POST',
                data: JSON.stringify(formData),
                dataType: 'application/json'
            }).then(() => {
                this.transitionToRoute('dashboard.bookings');
            }).catch(() => {
                this.transitionToRoute('dashboard.bookings');
            })
        },
          logout() {
            this.get('currentUser').logout();
            this.transitionToRoute('application');
          }

    }

});
