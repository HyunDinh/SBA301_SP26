import React, { createContext, useReducer, useContext } from 'react';

// 1. Khởi tạo trạng thái ban đầu
const initialState = {
  isAuthenticated: false,
  user: null,
};

// 2. Định nghĩa Reducer
const authReducer = (state, action) => {
  switch (action.type) {
    case 'LOGIN_SUCCESS':
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload,
      };
    case 'LOGOUT':
      return {
        ...state,
        isAuthenticated: false,
        user: null,
      };
    default:
      return state;
  }
};

const AuthContext = createContext();

// 3. Provider Component
export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  return (
    <AuthContext.Provider value={{ state, dispatch }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook để sử dụng Context nhanh hơn
export const useAuth = () => useContext(AuthContext);