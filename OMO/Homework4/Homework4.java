/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.k36.omo.hw.hw04;

import java.util.Map;

/**
 *
 * @author patrik
 */
public class Homework4 extends MessageVisitor {

    public Homework4(Peer peer) {
        super(peer);
    }

    // zde doplnte svuj kod
    @Override
    public boolean visitHaveMessage(HaveMessage message) {
        PeerInterface sender = message.getSender();
        int index = message.getBlockIndex();
        this.peer.peers2BlocksMap.get(sender)[index] = true;

        return false;
    }

    @Override
    public boolean visitRequestMessage(RequestMessage message) {
        PeerInterface sender = message.getSender();
        int index = message.getBlockIndex();
        byte[] data = this.peer.data[index];

        if (data != null) {
            sender.piece(this.peer, index, data);
        }

        return false;
    }

    @Override
    public boolean visitPieceMessage(PieceMessage message) {
        int index = message.getBlockIndex();
        Peer visitor = this.peer;

        visitor.data[index] = message.getData();
        ++visitor.downloadedBlocksCount;
        visitor.peers2BlocksMap.forEach((piece, data) -> piece.have(this.peer, index));

        return visitor.downloadedBlocksCount >= visitor.totalBlocksCount;
    }

    @Override
    public boolean visitIdleMessage(IdleMessage message) {
        Peer visitor = this.peer;
        int index = -1;
        int ownersNum = 0;
        PeerInterface owner = null;
        PeerInterface blockOwner;
        int blockOwnerCout;
        
        for (int i = 0; i < visitor.totalBlocksCount; i++) {
            blockOwner = null;
            blockOwnerCout = 0;
            
            for (Map.Entry<PeerInterface, boolean[]> value : visitor.peers2BlocksMap.entrySet()) {
                if (value.getValue()[i]) {
                    blockOwner = value.getKey();
                    ++blockOwnerCout;
                }
            }

            if (blockOwnerCout < ownersNum || owner == null) {
                index = i;
                ownersNum = blockOwnerCout;
                owner = blockOwner;
                
            }
        }
        
        if (owner != null && index >= 0 && ownersNum > 0) {
            owner.request(visitor, index);
        }

        return false;
    }
}
