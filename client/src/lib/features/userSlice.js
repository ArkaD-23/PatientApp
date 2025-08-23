import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { getProfile, updateUser } from "@/api/api";

export const fetchProfile = createAsyncThunk("user/get", async () => {
  const response = await getProfile();
  console.log("Profile response", response.data);
  return response.data;
});

export const userSlice = createSlice({
  name: "user",

  initialState: {
    data: {},
    status: "idle",
    updateStatus: "idle",
    error: null,
  },

  reducers: {},

  extraReducers(builder) {
    builder
      .addCase(fetchProfile.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchProfile.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.data = action.payload;
      })
      .addCase(fetchProfile.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message;
      })
  },
});

export const selectProfile = (state) => state.profile;

export default profileSlice.reducer;

