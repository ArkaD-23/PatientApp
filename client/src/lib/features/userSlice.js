import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

export const fetchUser = createAsyncThunk("user/get", async ({email, token}) => {
  const response = await fetch(`http://gateway:8090/v1/users/${email}/${token}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
  const data = await response.json();
  console.log("Profile response", data);
  return data;
});

export const userSlice = createSlice({
  name: "user",

  initialState: {
    data: {},
    status: "idle",
    error: null,
  },

  reducers: {},

  extraReducers(builder) {
    builder
      .addCase(fetchUser.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchUser.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.data = action.payload;
      })
      .addCase(fetchUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message;
      })
  },
});

export const selectUser = (state) => state.user;

export default userSlice.reducer;

