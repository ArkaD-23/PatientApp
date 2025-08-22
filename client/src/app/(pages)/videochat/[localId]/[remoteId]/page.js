import VideoChat from '@/components/VideoChat'
import React from 'react'

const page = async ({ params }) => {

  const { localId, remoteId } = await params;

  return (
    <div><VideoChat localId={localId} remoteId={remoteId}/></div>
  )
}

export default page;