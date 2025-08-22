import Chat from '@/components/Chat'
import React from 'react'

const page = async ({ params }) => {

  const { senderId, recipientId } = await params;

  return (
    <div><Chat senderId={senderId} recipientId={recipientId}/></div>
  )
}

export default page;