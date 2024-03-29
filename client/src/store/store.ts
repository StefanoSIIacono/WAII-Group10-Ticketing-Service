import { configureStore } from '@reduxjs/toolkit';
import authenticationReducer from './slices/authentication';
import ticketsReducer from './slices/tickets';
import expertsReducer from './slices/experts';
import errorsReducer from './slices/errors';

export const store = configureStore({
  reducer: {
    authenticate: authenticationReducer,
    tickets: ticketsReducer,
    experts: expertsReducer,
    errors: errorsReducer
  }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
