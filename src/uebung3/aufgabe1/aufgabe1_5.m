clear;
sequencesA = {'CCTTAGCCAATATCCGGGAA','ATGTAGCCAATATCCGGGAA','ACTGGATTGGTATCCGGGAA','ACTGGATCAAGTTCCGGGAA'};
sequencesNA = {'CCTTAGCCAATATCCGGGAA','ATGTAGCCAATATCCGGGAA','TCTGGATTGGTATCCGGGAA','ACTGGATCAAGTTCCGGGAA'};
%alignedSequencesA = multialign(sequencesA);
%alignedSequencesNA = multialign(sequencesNA);

%distancesA = seqpdist(sequencesA, 'PairwiseAlignment', true, 'SquareForm', true, 'Alphabet', 'NT');
%distancesNA = seqpdist(sequencesNA, 'PairwiseAlignment', true, 'SquareForm', true, 'Alphabet', 'NT');

distancesA = [0 3 8 7;3 0 9 8;8 9 0 5;7 8 5 0];
distancesNA = [0 3 8 7;3 0 10 8;8 10 0 6;7 8 6 0];

treeA = seqlinkage(distancesA, 'single', sequencesA);
treeNA = seqlinkage(distancesNA, 'single', sequencesNA);

view(treeA)
view(treeNA)