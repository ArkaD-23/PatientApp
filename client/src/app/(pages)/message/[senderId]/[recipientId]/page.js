import Chat from "@/components/Chat";
import LoggedInWrapper from "@/utils/LoggedInWrapper";
import React from "react";

const page = async ({ params }) => {
  const { senderId, recipientId } = await params;

  return (
    <LoggedInWrapper>
      <div>
        <Chat senderId={senderId} recipientId={recipientId} />
      </div>
    </LoggedInWrapper>
  );
};

export default page;
